package me.Cooltimmetje.StarBot.Utilities;

import me.Cooltimmetje.StarBot.Enums.EmojiEnum;
import me.Cooltimmetje.StarBot.Main;
import org.json.simple.JSONObject;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReactionAddEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class handles sending messages.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class MessagesUtils {

    /**
     * This HashMap is to save messages that have been reacted to, to recall their original message so we can print it out when people react to it.
     */
    public static HashMap<IMessage, JSONObject> reactions = new HashMap<>();

    /**
     * This adds a reaction to the specified message with the specified emoji. The debug string get's saved to recall later and will be posted upon reaction from the original author with the same emoji.
     *
     * @param message The message that we want to add the reaction to.
     * @param debug The message that will be saved. (Debug String)
     * @param emoji The emoji that we want to add.
     */
    @SuppressWarnings("unchecked")
    public static void addReactionMessage(IMessage message, String debug, EmojiEnum emoji){
        try {
            message.addReaction(emoji.getEmoji());
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();

        obj.put("time", System.currentTimeMillis());
        obj.put("debug", debug);
        obj.put("emoji", emoji.getEmoji());

        reactions.put(message, obj);
    }

    /**
     * EVENT: This will fire when a reaction was added to a message, then we will check if it meets the requirements to post the debug string.
     *
     * @param event The event that fires this.
     */
    @EventSubscriber
    public void onReaction(ReactionAddEvent event){
        if(reactions.containsKey(event.getMessage())){ //Check if the message is actually eligible for a "debug" string.
            if(event.getReaction().getClientReacted()){ //Check if the bot reacted the same.
                if(event.getReaction().getUserReacted(event.getMessage().getAuthor())){ //Check if the original author reacted.
                    JSONObject obj = reactions.get(event.getMessage()); //Save it for sake of code tidyness.
                    sendPlain(obj.get("emoji") + " " + obj.get("debug"), event.getMessage().getChannel()); //Post the message.

                    reactions.remove(event.getMessage()); //Remove it from the HashMap as we no longer need it there.
                }
            }
        }
    }

    /**
     * Send a message with a check mark in front.
     *
     * @param message The message that should be sent.
     * @param channel The channel where the message should be sent.
     */
    public static void sendSuccess(String message, IChannel channel) {
        try {
            channel.sendMessage(":white_check_mark: " + message);
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a plain message.
     *
     * @param msg The message that should be sent.
     * @param channel The channel where it should be sent.
     * @return The message that has been sent.
     */
    @SuppressWarnings("all") //Just because IntelliJ decided to be a dick.
    public static IMessage sendPlain(String msg, IChannel channel) {
        try {
            IMessage message = channel.sendMessage(msg);
            return message;
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Send a PM to a user.
     *
     * @param user User that we should send a message to.
     * @param message The message that should be sent.
     * @return The message that has been sent.
     */
    public static IMessage sendPM(IUser user, String message) {
        try {
            return Main.getInstance().getStarBot().getOrCreatePMChannel(user).sendMessage(message);

        } catch (DiscordException | RateLimitException | MissingPermissionsException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This adds a reaction of the given emoji to a message, but this will not return a message upon reaction from original author.
     *
     * @param message The message that we should add the reaction to.
     * @param emoji The emoji that we want to add.
     */
    public static void addReaction(IMessage message, EmojiEnum emoji){
        try {
            message.addReaction(emoji.getEmoji());
        } catch (MissingPermissionsException | RateLimitException | DiscordException e) {
            e.printStackTrace();
        }
    }

}