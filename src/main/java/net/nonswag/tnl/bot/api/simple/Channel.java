package net.nonswag.tnl.bot.api.simple;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.nonswag.core.api.file.formats.JsonFile;
import net.nonswag.tnl.bot.Bot;

import javax.annotation.Nullable;

@Getter
public class Channel<T extends net.dv8tion.jda.api.entities.channel.Channel> {
    private static final JsonFile config = new JsonFile("configs", "channels.json");

    public static final Channel<TextChannel> TODO = new Channel<>(TextChannel.class, "todo-id");
    public static final Channel<TextChannel> BOOSTS = new Channel<>(TextChannel.class, "discord-boosts-id");
    public static final Channel<TextChannel> SERVER_ENTRANCE = new Channel<>(TextChannel.class, "discord-entrance-id");
    public static final Channel<TextChannel> SERVER_EXIT = new Channel<>(TextChannel.class, "discord-exit-id");
    public static final Channel<VoiceChannel> CREATE_VOICE_CHANNEL = new Channel<>(VoiceChannel.class, "create-voice-id");

    @Nullable
    private final T channel;
    private final String configKey;
    private final long id;

    public Channel(Class<T> clazz, String configKey) {
        this.configKey = configKey;
        JsonObject root = config.getRoot().getAsJsonObject();
        this.id = root.has(getConfigKey()) ? root.get(getConfigKey()).getAsLong() : 0;
        this.channel = Bot.getJda().getChannelById(clazz, getId());
        root.addProperty(getConfigKey(), this.id);
    }

    public static void init() {
        config.save();
    }
}
