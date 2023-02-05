package net.nonswag.tnl.bot.api.mention;

import net.nonswag.core.api.annotation.MethodsReturnNullableByDefault;

@MethodsReturnNullableByDefault
public enum Mention {
    EVERYONE, HERE, NONE;

    public String tag() {
        return switch (this) {
            case EVERYONE -> "@everyone";
            case HERE -> "@here";
            case NONE -> null;
        };
    }
}
