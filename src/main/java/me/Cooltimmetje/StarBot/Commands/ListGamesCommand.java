package me.Cooltimmetje.StarBot.Commands;

import me.Cooltimmetje.StarBot.Utilities.Constants;
import me.Cooltimmetje.StarBot.Utilities.MessagesUtils;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Command that lists all available games.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class ListGamesCommand {

    public static void run(IMessage message) {
        StringBuilder sb = new StringBuilder();
        for(String game : Constants.games.keySet()){
            sb.append(game).append(", ");
        }

        String games = sb.toString();
        MessagesUtils.sendPlain("**All available games: **(separated by comma's) \n```\n" + games.substring(0, games.length() -2) + "\n```\nCan't find your game? Add it with `!creategame <name>`.", message.getChannel());
    }

}
