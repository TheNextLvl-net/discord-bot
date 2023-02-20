package net.nonswag.tnl.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.nonswag.core.Core;
import net.nonswag.core.api.annotation.FieldsAreNonnullByDefault;
import net.nonswag.core.api.annotation.MethodsReturnNonnullByDefault;
import net.nonswag.core.api.file.formats.GsonFile;
import net.nonswag.core.api.logger.ColoredPrintStream;
import net.nonswag.tnl.bot.api.command.CommandListener;
import net.nonswag.tnl.bot.api.command.CommandRegisterException;
import net.nonswag.tnl.bot.api.config.Config;
import net.nonswag.tnl.bot.api.simple.Channel;
import net.nonswag.tnl.bot.api.simple.Emoji;
import net.nonswag.tnl.bot.commands.*;
import net.nonswag.tnl.bot.listener.MemberListener;
import net.nonswag.tnl.bot.listener.TextListener;
import net.nonswag.tnl.bot.listener.VoiceListener;
import net.nonswag.tnl.bot.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Bot {
    @Nullable
    private static JDA jda = null;
    public static final List<CommandListener> COMMAND_LISTENERS = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(Bot.class);
    public static final Config config;

    static {
        GsonFile<Config> file = new GsonFile<>("configs", "config.json", new Config());
        if (!file.getFile().exists()) file.save();
        config = file.getRoot();
    }

    static {
        Core.init();
        Messages.init();
        ColoredPrintStream.debug.setCondition(() -> config.debug);
        ColoredPrintStream.trace.setCondition(() -> config.trace);
    }

    public static void main(String[] args) {
        setupJda();
        registerListeners();
        registerCommands();
        Channel.init();
        Emoji.init();
    }

    private static void registerListeners() {
        getJda().addEventListener(new TextListener());
        getJda().addEventListener(new VoiceListener());
        getJda().addEventListener(new MemberListener());
    }

    private static void registerCommands() {
        try {
            registerCommand(SayCommand.getInstance());
            registerCommand(PinCommand.getInstance());
            registerCommand(HelpCommand.getInstance());
            registerCommand(CastCommand.getInstance());
            registerCommand(MembersCommand.getInstance());
            if (Channel.TODO.getChannel() != null) registerCommand(ToDoCommand.getInstance());
        } catch (CommandRegisterException e) {
            logger.error("Failed to register commands", e);
        }
    }

    private static void setupJda() {
        try {
            JDABuilder builder = JDABuilder.createDefault(config.token);
            for (GatewayIntent intent : GatewayIntent.values()) builder.enableIntents(intent);
            builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
            builder.disableIntents(GatewayIntent.GUILD_MESSAGE_TYPING);
            builder.setLargeThreshold(50);
            jda = builder.build().awaitReady();
            if (config.status.isBlank()) return;
            jda.getPresence().setPresence(Activity.of(config.activity, config.status, config.streamUrl), config.idle);
        } catch (InterruptedException e) {
            logger.error("failed to setup jda", e);
            System.exit(1);
        }
    }

    public static JDA getJda() {
        assert jda != null : "failed to setup jda properly";
        return jda;
    }

    public static void registerCommand(CommandListener command) throws CommandRegisterException {
        String label = command.getCommand().toLowerCase();
        if (label.indexOf(' ') != -1) {
            throw new CommandRegisterException("Illegal Command structure for '/%s'".formatted(label));
        } else if (COMMAND_LISTENERS.contains(command)) {
            throw new CommandRegisterException("The Command '/%s' is already registered".formatted(label));
        } else COMMAND_LISTENERS.add(command);
    }

    public static void unregisterCommand(String command) {
        COMMAND_LISTENERS.removeIf(commandListener -> commandListener.getCommand().equalsIgnoreCase(command));
    }
}
