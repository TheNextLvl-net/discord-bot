package net.nonswag.tnl.bot.api.config;

import net.dv8tion.jda.api.entities.Activity;
import net.nonswag.core.api.annotation.FieldsAreNonnullByDefault;

@FieldsAreNonnullByDefault
public class Config {
    public String token = "token", streamUrl = "https://twitch.tv/example", status = "";
    public Activity.ActivityType activity = Activity.ActivityType.STREAMING;
    public boolean idle, debug, trace;
}
