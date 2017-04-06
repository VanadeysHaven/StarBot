package me.Cooltimmetje.StarBot.Commands.AdminCommands;

import me.Cooltimmetje.StarBot.Enums.EmojiEnum;
import me.Cooltimmetje.StarBot.Main;
import me.Cooltimmetje.StarBot.Utilities.Constants;
import me.Cooltimmetje.StarBot.Utilities.MessagesUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.Image;
import sx.blah.discord.util.RateLimitException;

import java.text.MessageFormat;

/**
 * Changes the avatar of the bot.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class ChangeAvatar {

    public static void run(IMessage message){
        if(Constants.admins.contains(message.getAuthor().getID())){
            String[] args = message.getContent().split(" ");

            if(args.length > 1){
                if(!args[1].endsWith(".png")){
                    MessagesUtils.addReactionMessage(message, "The URL must end with .png", EmojiEnum.X);
                    return;
                }
                try {
                    Main.getInstance().getStarBot().changeAvatar(Image.forUrl("png", args[1]));

                    MessagesUtils.addReactionMessage(message, "Changed image!", EmojiEnum.WHITE_CHECK_MARK);
                    MessagesUtils.sendPlain(MessageFormat.format(":camera: Admin **{0}#{1}** has changed the bot avatar to <{2}>.",
                            message.getAuthor().getName(), message.getAuthor().getDiscriminator(), args[1]),
                            Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
                } catch (DiscordException e) {
                    e.printStackTrace();
                    MessagesUtils.addReactionMessage(message, "That didn't work. Try again.", EmojiEnum.X);
                } catch (RateLimitException e) {
                    e.printStackTrace();
                    MessagesUtils.addReactionMessage(message, "You got rate limited, try again later.", EmojiEnum.X);
                }
            } else {
                MessagesUtils.addReactionMessage(message, "Please enter a URL to change the avatar to. (URL must end in '.png')", EmojiEnum.X);
            }
        }
    }

}
