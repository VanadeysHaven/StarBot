package me.Cooltimmetje.StarBot.Commands;

import me.Cooltimmetje.StarBot.Enums.EmojiEnum;
import me.Cooltimmetje.StarBot.Main;
import me.Cooltimmetje.StarBot.Utilities.Constants;
import me.Cooltimmetje.StarBot.Utilities.MessagesUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.text.MessageFormat;

/**
 * Class used for the commands to add and remove games from users.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class AddRemoveGameCommand {

    public static void add(IMessage message){
        if(message.getContent().split(" ").length > 1){
            String game = message.getContent().substring(9);
            if(Constants.games.containsKey(game)){
                if(!message.getAuthor().getRolesForGuild(message.getGuild()).contains(message.getGuild().getRoleByID(Constants.games.get(game)))){
                    try {
                        message.getAuthor().addRole(message.getGuild().getRoleByID(Constants.games.get(game)));
                    } catch (MissingPermissionsException e) {
                        MessagesUtils.addReactionMessage(message, "The bot has insufficient permissions to complete this action, please report this to Timmy or Jasch.", EmojiEnum.X);
                        e.printStackTrace();
                    } catch (RateLimitException e) {
                        MessagesUtils.addReactionMessage(message, "The bot has reached it's rate limit, please wait and try again.", EmojiEnum.X);
                        e.printStackTrace();
                    } catch (DiscordException e) {
                        MessagesUtils.addReactionMessage(message, "Discord returned a unexpected exception. Please wait and try again, if this keeps happening please contact Timmy or Jasch.", EmojiEnum.X);
                        e.printStackTrace();
                    }

                    MessagesUtils.sendPlain(MessageFormat.format(":fast_forward: **{0}#{1}** `ID: {2}` has added game **{3}**.",
                            message.getAuthor().getName(), message.getAuthor().getDiscriminator(), message.getAuthor().getID(), game), Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
                    MessagesUtils.addReactionMessage(message, "Game `" + game + "` has been added!", EmojiEnum.WHITE_CHECK_MARK);
                } else {
                    MessagesUtils.addReactionMessage(message, "You already have that game added, you can remove it with `!removegame " + game + "`", EmojiEnum.X);
                }
            } else {
                MessagesUtils.addReactionMessage(message, "That game does not exist. Game names are CaSe SeNsItIvE.", EmojiEnum.X);
            }
        } else {
            MessagesUtils.addReactionMessage(message, "Please specify a game.", EmojiEnum.X);
        }
    }

    public static void remove(IMessage message){
        if(message.getContent().split(" ").length > 1){
            String game = message.getContent().substring(12);
            if(Constants.games.containsKey(game)){
                if(message.getAuthor().getRolesForGuild(message.getGuild()).contains(message.getGuild().getRoleByID(Constants.games.get(game)))){
                    try {
                        message.getAuthor().removeRole(message.getGuild().getRoleByID(Constants.games.get(game)));
                    } catch (MissingPermissionsException e) {
                        MessagesUtils.addReactionMessage(message, "The bot has insufficient permissions to complete this action, please report this to Timmy or Jasch.", EmojiEnum.X);
                        e.printStackTrace();
                    } catch (RateLimitException e) {
                        MessagesUtils.addReactionMessage(message, "The bot has reached it's rate limit, please wait and try again.", EmojiEnum.X);
                        e.printStackTrace();
                    } catch (DiscordException e) {
                        MessagesUtils.addReactionMessage(message, "Discord returned a unexpected exception. Please wait and try again, if this keeps happening please contact Timmy or Jasch.", EmojiEnum.X);
                        e.printStackTrace();
                    }

                    MessagesUtils.sendPlain(MessageFormat.format(":rewind: **{0}#{1}** `ID: {2}` has removed game **{3}**.",
                            message.getAuthor().getName(), message.getAuthor().getDiscriminator(), message.getAuthor().getID(), game), Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
                    MessagesUtils.addReactionMessage(message, "Game `" + game + "` has been removed!", EmojiEnum.WHITE_CHECK_MARK);
                } else {
                    MessagesUtils.addReactionMessage(message, "You do not have that game added, you can add it with `!addgame " + game + "`", EmojiEnum.X);
                }
            } else {
                MessagesUtils.addReactionMessage(message, "That game does not exist. Game names are CaSe SeNsItIvE.", EmojiEnum.X);
            }
        } else {
            MessagesUtils.addReactionMessage(message, "Please specify a game.", EmojiEnum.X);
        }
    }

}
