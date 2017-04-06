package me.Cooltimmetje.StarBot.Commands.AdminCommands;

import me.Cooltimmetje.StarBot.Enums.EmojiEnum;
import me.Cooltimmetje.StarBot.Main;
import me.Cooltimmetje.StarBot.Utilities.Constants;
import me.Cooltimmetje.StarBot.Utilities.MessagesUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

import java.text.MessageFormat;

/**
 * This can change the name of the bot to something.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class ChangeName {

    public static void run(IMessage message){
        if(Constants.admins.contains(message.getAuthor().getID())){
            String[] args = message.getContent().split(" ");

            if(args.length > 1){
                try {
                    Main.getInstance().getStarBot().changeUsername(message.getContent().substring(12).trim());

                    MessagesUtils.addReactionMessage(message, "Bot name changed to: `" + message.getContent().substring(12).trim() + "`", EmojiEnum.WHITE_CHECK_MARK);
                    MessagesUtils.sendPlain(MessageFormat.format(":label: Admin **{0}#{1}** has changed the bot name to {2}.",
                            message.getAuthor().getName(), message.getAuthor().getDiscriminator(), message.getContent().substring(12).trim()),
                            Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
                } catch (DiscordException e) {
                    e.printStackTrace();
                    MessagesUtils.addReactionMessage(message, "That didn't work. Try again.", EmojiEnum.X);
                } catch (RateLimitException e) {
                    e.printStackTrace();
                    MessagesUtils.addReactionMessage(message, "You got rate limited, try again later.", EmojiEnum.X);
                }
            } else {
                MessagesUtils.addReactionMessage(message, "Please specify a name!", EmojiEnum.X);
            }
        }
    }

}
