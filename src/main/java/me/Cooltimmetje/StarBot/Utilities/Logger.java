package me.Cooltimmetje.StarBot.Utilities;

import org.slf4j.LoggerFactory;

/**
 * Logger class.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class Logger {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);

    /**
     * Send a info string to the log.
     *
     * @param string Message that should be logged.
     */
    public static void info(String string){
        log.info(string);
    }

    /**
     * Log a warning
     *
     * @param string Message that should be sent.
     * @param e exception that was involved.
     */
    public static void warn(String string, Exception e){
        log.warn(string,e);
    }


}
