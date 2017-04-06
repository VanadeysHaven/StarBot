package me.Cooltimmetje.StarBot.Commands;

import me.Cooltimmetje.StarBot.Commands.AdminCommands.ChangeAvatar;
import me.Cooltimmetje.StarBot.Commands.AdminCommands.ChangeName;
import me.Cooltimmetje.StarBot.Enums.EmojiEnum;
import me.Cooltimmetje.StarBot.Utilities.MessagesUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * This class handles the triggering of commands.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class CommandManager {
    @EventSubscriber
    public void onMessage(MessageReceivedEvent event){
        if(!event.getMessage().getChannel().isPrivate()){
            switch (event.getMessage().getContent().split(" ")[0]){
                case "!changename":
                    ChangeName.run(event.getMessage());
                    break;
                case "!changeavatar":
                    ChangeAvatar.run(event.getMessage());
                    break;
//                case "!ts":
//                    MessagesUtils.sendPlain("92.222.104.72:9989", event.getMessage().getChannel());
//                    break;
                case "!ping":
                    MessagesUtils.addReactionMessage(event.getMessage(), "PONG!", EmojiEnum.PING_PONG);
                    break;
                case "!creategame":
                    CreateEditDeleteGameCommand.create(event.getMessage());
                    break;
                case "!editgame":
                    CreateEditDeleteGameCommand.edit(event.getMessage());
                    break;
                case "!deletegame":
                    CreateEditDeleteGameCommand.delete(event.getMessage());
                    break;
                case "!addgame":
                    AddRemoveGameCommand.add(event.getMessage());
                    break;
                case "!removegame":
                    AddRemoveGameCommand.remove(event.getMessage());
                    break;
                case "!listgames":
                    ListGamesCommand.run(event.getMessage());
                    break;
            }
        } else {
            switch (event.getMessage().getContent().split(" ")[0]){

            }
        }
    }

}
