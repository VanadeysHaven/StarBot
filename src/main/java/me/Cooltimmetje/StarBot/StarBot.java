package me.Cooltimmetje.StarBot;

import me.Cooltimmetje.StarBot.Commands.CommandManager;
import me.Cooltimmetje.StarBot.Database.MySqlManager;
import me.Cooltimmetje.StarBot.Enums.EmojiEnum;
import me.Cooltimmetje.StarBot.Listeners.JoinQuitListener;
import me.Cooltimmetje.StarBot.Utilities.Constants;
import me.Cooltimmetje.StarBot.Utilities.Logger;
import me.Cooltimmetje.StarBot.Utilities.MessagesUtils;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MentionEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.Status;
import sx.blah.discord.util.DiscordException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The bot instance.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class StarBot {

    private volatile IDiscordClient starBot;
    private String token;
    private final AtomicBoolean reconnect = new AtomicBoolean(true);
    private boolean preListenersReady = false;
    private boolean listenersReady = false;

    /**
     * Create a instance of the bot.
     *
     * @param token The Discord token that should be used to login.
     */
    public StarBot(String token) {
        this.token = token;
    }

    /**
     * Get the bot logged in to Discord, and register the core listeners.
     *
     * @throws DiscordException Gets thrown when something happens during logging in.
     */
    public void login() throws DiscordException {
        starBot = new ClientBuilder().withToken(token).login();
        if(!preListenersReady){
            starBot.getDispatcher().registerListener(this);

            preListenersReady = true;
        }
    }

    /**
     * This registers all other listeners.
     *
     * @param event The event that triggers this bit of code.
     */
    @EventSubscriber
    public void onReady(ReadyEvent event){
        if(!listenersReady){
            event.getClient().changeStatus(Status.game("Being a Twitch Star!"));
            starBot.getDispatcher().registerListener(new CommandManager());
            starBot.getDispatcher().registerListener(new MessagesUtils());
            starBot.getDispatcher().registerListener(new JoinQuitListener());

            MySqlManager.loadAdmins();
            MySqlManager.loadGames();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> terminate(true)));

            MessagesUtils.sendPlain(":robot: Startup Sequence Complete!", Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
            listenersReady = true;
        }
    }

    /**
     * Used to logout the bot from Discord.
     *
     * @param event The event that triggers this bit of code.
     */
    @EventSubscriber
    public void onMention(MentionEvent event){
        if(event.getMessage().getContent().split(" ").length > 1) {
            if (event.getMessage().getContent().split(" ")[1].equalsIgnoreCase("logout")) {
                if (event.getMessage().getAuthor().getID().equals(Constants.TIMMY_ID) || event.getMessage().getAuthor().getID().equals(Constants.JASCH_ID)) {
                    MessagesUtils.sendSuccess("Well, okay then...\n`Shutting down...`", event.getMessage().getChannel());

                    terminate(false);
                } else {
                    MessagesUtils.addReactionMessage(event.getMessage(), "Ur not timmy or jasch >=(", EmojiEnum.X);
                }
            }
        }
    }

    /**
     * Terminate the bot. This will prevent it from reconnecting.
     * @param sigterm If the terminate was demanded by SIGTERM
     */
    private void terminate(boolean sigterm) {
        if(sigterm){
            MessagesUtils.sendPlain(":robot: Logging out due to SIGTERM...", Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
        } else {
            MessagesUtils.sendPlain(":robot: Logging out due to command...", Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
        }
        reconnect.set(false);
        try {
            MySqlManager.disconnect();
            starBot.logout();
        } catch (DiscordException e) {
            Logger.warn("Couldn't log out.", e);
        }

    }

    /**
     * Get the bot.
     *
     * @return Current instance of StarBot.
     */
    public IDiscordClient getStarBot(){
        return starBot;
    }
}
