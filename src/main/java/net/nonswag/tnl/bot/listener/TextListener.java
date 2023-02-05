package net.nonswag.tnl.bot.listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.nonswag.tnl.bot.Bot;
import net.nonswag.tnl.bot.api.command.Command;
import net.nonswag.tnl.bot.api.command.CommandEvent;
import net.nonswag.tnl.bot.api.command.CommandListener;
import net.nonswag.tnl.bot.api.permission.PermissionHelper;
import net.nonswag.tnl.bot.api.simple.Channel;
import net.nonswag.tnl.bot.api.simple.Emoji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TextListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUser() == null || event.getMember() == null || event.getUser().isBot()) return;
        if (!event.getChannel().equals(Channel.TODO.getChannel())) return;
        if (!(event.getEmoji() instanceof CustomEmoji emoji)) return;
        if (emoji.equals(Emoji.YES.getEmoji())) event.retrieveMessage().complete().delete().queue();
        else event.getReaction().removeReaction(event.getUser()).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        StringBuilder sb = new StringBuilder();
        for (OptionMapping option : event.getOptions()) sb.append(option.getAsString());
        for (CommandListener listener : Bot.COMMAND_LISTENERS) {
            if (!listener.getCommand().equalsIgnoreCase(event.getName())) continue;
            listener.onSlashCommand(new Command(event.getName(), List.of(sb.toString().split(" "))), event);
            break;
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem() || event.getMember() == null) return;
        if (!PermissionHelper.hasPermission(event.getMember(), event.getChannel(), Permission.USE_APPLICATION_COMMANDS)) {
            event.getMessage().delete().queueAfter(250, TimeUnit.MILLISECONDS);
            return;
        }
        String messageText = event.getMessage().getContentRaw();
        Guild guild = event.isFromGuild() ? event.getGuild() : null;
        if (event.getChannel().getType().equals(ChannelType.NEWS) && !messageText.toLowerCase().startsWith("/")) {
            messageText = "/say " + messageText;
        } else if (event.getChannel().equals(Channel.TODO.getChannel()) && !messageText.toLowerCase().startsWith("/")) {
            messageText = "/todo " + messageText;
        }
        if (!messageText.startsWith("/")) return;
        String[] split = messageText.replaceFirst("/", "").split(" ");
        CommandEvent commandEvent;
        if (split.length > 1) {
            commandEvent = new CommandEvent(event.getJDA(), event.getAuthor(), guild, event.getMember(), event.getChannel(),
                    event.getMessage(), new Command(split[0], Arrays.asList(split).subList(1, split.length)));
        } else {
            commandEvent = new CommandEvent(event.getJDA(), event.getAuthor(), guild, event.getMember(), event.getChannel(),
                    event.getMessage(), new Command(split[0], new ArrayList<>()));
        }
        for (CommandListener commandListener : Bot.COMMAND_LISTENERS) {
            if (!commandListener.getCommand().equalsIgnoreCase(split[0])) continue;
            commandListener.onCommand(commandEvent.getJda(), commandEvent.getGuild(), commandEvent.getMember(), commandEvent.getChannel(), commandEvent.getMessage(), commandEvent.getCommand());
            return;
        }
        event.getChannel().sendMessage("The command (/" + commandEvent.getCommand().getCommand().toLowerCase() + ") doesn't exist").complete().delete().queueAfter(10, TimeUnit.SECONDS);
        event.getMessage().delete().queueAfter(250, TimeUnit.MILLISECONDS);
    }
}
