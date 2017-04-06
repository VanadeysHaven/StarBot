package me.Cooltimmetje.StarBot.Enums;

import lombok.Getter;

/**
 * This is to easily recall emoji's without going out and copying them.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
@Getter
public enum EmojiEnum {

    WHITE_CHECK_MARK    ("✅"),
    ARROW_UP            ("⬆"),
    WARNING             ("⚠"),
    X                   ("❌"),
    PING_PONG           ("🏓"),
    NO_ENTRY            ("⛔"),
    WASTEBASKET         ("🗑");

    private String emoji;

    EmojiEnum(String s){
        this.emoji = s;
    }

}
