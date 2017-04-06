package me.Cooltimmetje.StarBot.Commands;

import me.Cooltimmetje.StarBot.Database.MySqlManager;
import me.Cooltimmetje.StarBot.Enums.EmojiEnum;
import me.Cooltimmetje.StarBot.Main;
import me.Cooltimmetje.StarBot.Utilities.Constants;
import me.Cooltimmetje.StarBot.Utilities.Logger;
import me.Cooltimmetje.StarBot.Utilities.MessagesUtils;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RoleBuilder;

import java.text.MessageFormat;
import java.util.EnumSet;

/**
 * This command can be used by everyone to create a new game role, or by admins only to edit or remove a game.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class CreateEditDeleteGameCommand {

    public static void create(IMessage message){
        if(message.getContent().split(" ").length > 1){
            String game = message.getContent().substring(12);
            if(game.length() <= 128) {
                if (!Constants.games.containsKey(game)) {
                    IGuild guild = Main.getInstance().getStarBot().getGuildByID(Constants.SERVER_ID);
                    RoleBuilder rb = new RoleBuilder(guild).withName(game).setMentionable(true).withPermissions(EnumSet.of(Permissions.VOICE_CONNECT));
                    IRole role = null;
                    try {
                        role = rb.build();
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

                    if(role == null){
                        MessagesUtils.addReactionMessage(message, "Something went wrong, try again or contact Timmy or Jasch.", EmojiEnum.X);
                        return;
                    }
                    MySqlManager.addGame(role);
                    Constants.games.put(game, role.getID());

                    MessagesUtils.sendPlain(MessageFormat.format(":new: **{0}#{1}** `ID: {2}` has created a new game with the name **{3}** `Role ID: {4}`.",
                            message.getAuthor().getName(), message.getAuthor().getDiscriminator(), message.getAuthor().getID(), game, role.getID()),
                            Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
                    MessagesUtils.addReactionMessage(message, "Game was added, you may now assign the role using `!addgame " + game + "`!", EmojiEnum.WHITE_CHECK_MARK);
                } else {
                    MessagesUtils.addReactionMessage(message, "This game already exists, join it using `!addgame " + game + "`.", EmojiEnum.X);
                }
            } else {
                MessagesUtils.addReactionMessage(message, "The name is too long, maximum length is 128 characters.", EmojiEnum.X);
            }
        } else {
            MessagesUtils.addReactionMessage(message, "Please specify a game.", EmojiEnum.X);
        }
    }

    public static void edit(IMessage message){
        if(Constants.admins.contains(message.getAuthor().getID()) || message.getAuthor().getRolesForGuild(message.getGuild()).contains(message.getGuild().getRolesByName("STAFF").get(0))) {
            if (message.getContent().split(" ").length > 1) {
                if(message.getContent().substring(10).split("\\|").length == 2) {
                    String game = message.getContent().substring(10).split("\\|")[0];
                    Logger.info(game);
                    String newName = message.getContent().substring(10).split("\\|")[1];
                    Logger.info(newName);
                    if (Constants.games.containsKey(game)) {
                        IRole role = message.getGuild().getRoleByID(Constants.games.get(game));
                        try {
                           role.changeName(newName);
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

                        MySqlManager.updateGame(game, newName);
                        Constants.games.remove(game);
                        Constants.games.put(newName, role.getID());

                        MessagesUtils.sendPlain(MessageFormat.format(":pencil2: Staff member **{0}#{1}** has edited the game **{2}** to **{3}**.",
                                message.getAuthor().getName(), message.getAuthor().getDiscriminator(), game, newName),
                                Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
                        MessagesUtils.addReactionMessage(message, "Game `" + game + "` changed to `" + newName + "`!", EmojiEnum.WHITE_CHECK_MARK);
                    } else {
                        MessagesUtils.addReactionMessage(message, "That game does not exist. Game names are CaSe SeNsItIvE.", EmojiEnum.X);
                    }
                } else {
                    MessagesUtils.addReactionMessage(message, "Wrong arguments: `!editgame <old name>|<new name>`", EmojiEnum.X);
                }
            } else {
                MessagesUtils.addReactionMessage(message, "Please specify a game.", EmojiEnum.X);
            }
        } else {
            MessagesUtils.addReaction(message, EmojiEnum.NO_ENTRY);
        }
    }

    public static void delete(IMessage message){
        if(Constants.admins.contains(message.getAuthor().getID()) || message.getAuthor().getRolesForGuild(message.getGuild()).contains(message.getGuild().getRolesByName("STAFF").get(0))){
            if(message.getContent().split(" ").length > 1){
                String game = message.getContent().substring(12);
                if(Constants.games.containsKey(game)){
                    try {
                        message.getGuild().getRoleByID(Constants.games.get(game)).delete();
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

                    MySqlManager.deleteGame(game);
                    Constants.games.remove(game);

                    MessagesUtils.sendPlain(MessageFormat.format(":wastebasket: Staff member **{0}#{1}** has removed the game **{2}**.",
                            message.getAuthor().getName(), message.getAuthor().getDiscriminator(), game),
                            Main.getInstance().getStarBot().getChannelByID(Constants.LOG_CHANNEL));
                    MessagesUtils.addReactionMessage(message, "Game `" + game + "` removed!", EmojiEnum.WHITE_CHECK_MARK);
                } else {
                    MessagesUtils.addReactionMessage(message, "That game does not exist. Game names are CaSe SeNsItIvE.", EmojiEnum.X);
                }
            } else {
                MessagesUtils.addReactionMessage(message, "Please specify a game.", EmojiEnum.X);
            }
        } else {
            MessagesUtils.addReaction(message, EmojiEnum.NO_ENTRY);
        }
    }

}
