package me.Cooltimmetje.StarBot.Listeners;

import me.Cooltimmetje.StarBot.Main;
import me.Cooltimmetje.StarBot.Utilities.Constants;
import me.Cooltimmetje.StarBot.Utilities.MessagesUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.UserJoinEvent;
import sx.blah.discord.handle.impl.events.UserLeaveEvent;

import java.text.MessageFormat;

/**
 * Listens for joins and quits.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class JoinQuitListener {

    @EventSubscriber
    public void onJoin(UserJoinEvent event){
        MessagesUtils.sendPlain(MessageFormat.format(":arrow_right: **{0}#{1}** `ID: {2}` joined the server.",
                event.getUser().getName(), event.getUser().getDiscriminator(), event.getUser().getID()),
                Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
    }

    @EventSubscriber
    public void onLeave(UserLeaveEvent event){
        MessagesUtils.sendPlain(MessageFormat.format(":arrow_left: **{0}#{1}** `ID: {2}` left the server.",
                event.getUser().getName(), event.getUser().getDiscriminator(), event.getUser().getID()),
                Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
    }

}
