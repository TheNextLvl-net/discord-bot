package net.nonswag.tnl.bot.utils;

import net.nonswag.core.api.annotation.FieldsAreNonnullByDefault;
import net.nonswag.core.api.message.Message;
import net.nonswag.core.api.message.key.SystemMessageKey;

@FieldsAreNonnullByDefault
public class Messages {
    public static final SystemMessageKey MEMBER_BOOSTED_SERVER = new SystemMessageKey("member-boosted-server").register();
    public static final SystemMessageKey MEMBER_JOINED_SERVER = new SystemMessageKey("member-joined-server").register();
    public static final SystemMessageKey MEMBER_LEFT_SERVER = new SystemMessageKey("member-left-server").register();

    public static void init() {
        Message.ROOT.setDefault(MEMBER_BOOSTED_SERVER, "> %user% boosted the discord");
        Message.ROOT.setDefault(MEMBER_JOINED_SERVER, "> %user% joined the discord");
        Message.ROOT.setDefault(MEMBER_LEFT_SERVER, "> %user% left the discord");
        Message.ROOT.save();
    }
}
