package net.nonswag.tnl.bot.api.simple;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.nonswag.core.api.file.formats.JsonFile;
import net.nonswag.tnl.bot.Bot;

import javax.annotation.Nullable;

@Getter
public class Emoji {
    private static final JsonFile config = new JsonFile("configs", "emojis.json");

    public static final Emoji YES = new Emoji("yes-id");

    @Nullable
    private final RichCustomEmoji emoji;
    private final String configKey;
    private final long id;

    public Emoji(String configKey) {
        this.configKey = configKey;
        JsonObject root = config.getRoot().getAsJsonObject();
        this.id = root.has(getConfigKey()) ? root.get(getConfigKey()).getAsLong() : 0;
        this.emoji = Bot.getJda().getEmojiById(getId());
        root.addProperty(getConfigKey(), this.id);
    }

    public static void init() {
        config.save();
    }
}
