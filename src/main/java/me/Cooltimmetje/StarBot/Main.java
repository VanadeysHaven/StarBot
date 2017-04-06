package me.Cooltimmetje.StarBot;

import me.Cooltimmetje.StarBot.Database.MySqlManager;
import me.Cooltimmetje.StarBot.Utilities.Constants;
import me.Cooltimmetje.StarBot.Utilities.TenMinuteTimer;
import org.slf4j.LoggerFactory;
import sx.blah.discord.util.DiscordException;

import java.util.Timer;

/**
 * This is the Main class, this handles starting up and alot of other things.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class Main {

    private static StarBot starBot;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);
    private static Timer timer = new Timer();

    /**
     * Main method, this gets called when the bot starts up.
     *
     * @param args The program arguments, contains passwords and tokens.
     */
    public static void main(String[] args){
        if(args.length < 3) {
            throw new IllegalArgumentException("Oi m8, I need a discord token, mysql username and pass m9");
        } else {
            log.info("Connecting to database...");
            MySqlManager.setupHikari(args[1],args[2]);

            log.info("Setting up Discord connection...");
            starBot = new StarBot(args[0]);
        }

        log.info("All systems operational, ready to connect to Discord.");

        try {
            starBot.login();

            log.info("Connected! ALL THE MEMES!");
        } catch (DiscordException e) {
            e.printStackTrace();
        }

        timer.schedule(new TenMinuteTimer(), Constants.INACTIVE_DELAY, Constants.INACTIVE_DELAY);
        Constants.STARTUP_TIME = System.currentTimeMillis();
    }

    /**
     * Get the star bot instance.
     *
     * @return The instance.
     */
    public static StarBot getInstance(){
        return starBot;
    }

}
