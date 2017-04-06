package me.Cooltimmetje.StarBot.Utilities;

import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Just a timer. 10 minute interval. Nothing fancy.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class TenMinuteTimer extends TimerTask {

    @Override
    public void run() {
        ArrayList<IMessage> temp = new ArrayList<>();
        for (IMessage message : MessagesUtils.reactions.keySet()){
            long time = Long.parseLong(MessagesUtils.reactions.get(message).get("time")+"");
            if((System.currentTimeMillis() - time) > (30*60*1000)){
                temp.add(message);
            }
        }
        for(IMessage message : temp){
            MessagesUtils.reactions.remove(message);
        }
    }

}
