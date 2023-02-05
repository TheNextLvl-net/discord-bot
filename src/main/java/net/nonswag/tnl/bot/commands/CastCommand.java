package net.nonswag.tnl.bot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.nonswag.tnl.bot.api.command.Command;
import net.nonswag.tnl.bot.api.command.CommandListener;
import net.nonswag.tnl.bot.api.embed.Embed;
import net.nonswag.tnl.bot.api.mention.Mention;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CastCommand extends CommandListener {
    @Getter
    private static final CastCommand instance = new CastCommand();
    private static final Logger logger = LoggerFactory.getLogger(SayCommand.class);

    private CastCommand() {
        super("cast [channel] [message] [mention]", "Let the bot say something in another channel");
        OptionData mention = new OptionData(OptionType.STRING, "mention", "additional mention", false);
        for (Mention value : Mention.values()) mention.addChoice(value.name().toLowerCase(), value.name());
        getSlashCommand().editCommand().addOption(OptionType.CHANNEL, "channel", "target channel", true).
                addOption(OptionType.STRING, "message", "message to send", true).addOptions(mention).queue();
    }

    @Override
    public void onCommand(JDA jda, @Nullable Guild guild, @Nullable Member member, MessageChannelUnion channel, Message message, Command command) {
        new Thread(() -> {
            if (guild == null) {
                channel.sendMessage("This action cannot be performed here").complete().delete().queueAfter(10, TimeUnit.SECONDS);
                return;
            }
            if (!hasPermission(member, channel, Permission.MESSAGE_MANAGE)) {
                channel.sendMessage("You have no rights (MESSAGE_MANAGE)").complete().delete().queueAfter(10, TimeUnit.SECONDS);
                return;
            }
            assert member != null;
            List<String> args = command.getArguments();
            if (args.size() >= 2 || (!message.getAttachments().isEmpty() && args.size() >= 1)) {
                String id = args.get(0).replaceAll("[^0-9]", "");
                StandardGuildMessageChannel target = guild.getTextChannelById(id);
                if (target == null) target = guild.getNewsChannelById(id);
                if (target == null) {
                    channel.sendMessage("The first argument has to be a message channel").complete().delete().queueAfter(10, TimeUnit.SECONDS);
                    return;
                }
                String say;
                if (args.size() == 1) say = " ";
                else say = String.join(" ", args.subList(1, args.size()));
                Mention mention = Mention.NONE;
                for (Mention value : Mention.values()) {
                    if (say.toLowerCase().endsWith(value.name().toLowerCase())) {
                        mention = value;
                        say = say.substring(0, say.length() - value.name().length());
                        break;
                    }
                }
                MessageCreateAction action;
                boolean embed = false;
                if (say.startsWith("embed:")) {
                    say = say.substring(6, Math.min(say.length(), 256));
                    embed = true;
                }
                if (embed) action = target.sendMessageEmbeds(Embed.create(say, member));
                else action = target.sendMessage(say);
                for (Message.Attachment attachment : message.getAttachments()) {
                    target.sendTyping().queue();
                    var future = attachment.getProxy().download();
                    future.thenAccept(result -> action.addFiles(FileUpload.fromData(result, attachment.getFileName())));
                }
                String tag = mention.tag();
                if (tag != null) {
                    action.complete().reply(tag).complete().delete().queueAfter(250, TimeUnit.MILLISECONDS);
                } else action.queue();
            } else if (args.isEmpty()) {
                channel.sendMessage("No target channel defined").complete().delete().queueAfter(10, TimeUnit.SECONDS);
            } else channel.sendMessage("Found no content to send").complete().delete().queueAfter(10, TimeUnit.SECONDS);
        }).start();
        message.delete().queueAfter(250, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onSlashCommand(Command command,  SlashCommandInteractionEvent event) {
        OptionMapping channel = event.getOption("channel");
        OptionMapping message = event.getOption("message");
        OptionMapping option = event.getOption("mention");
        Mention mention = option == null ? Mention.NONE : Mention.valueOf(option.getAsString());
        Member member = event.getMember();
        if (!hasPermission(member, event.getChannel(), Permission.MESSAGE_MANAGE)) {
            event.reply("You have no rights (MESSAGE_MANAGE)").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
        } else if (channel != null && message != null && member != null) {
            if (channel.getType().equals(OptionType.CHANNEL)) {
                GuildChannelUnion target = channel.getAsChannel();
                String say = message.getAsString();
                MessageCreateAction action;
                boolean embed = false;
                if (say.startsWith("embed:")) {
                    say = say.substring(6, Math.min(say.length(), 256));
                    embed = true;
                }
                if (embed) action = target.asStandardGuildMessageChannel().sendMessageEmbeds(Embed.create(say, member));
                else action = target.asStandardGuildMessageChannel().sendMessage(say);
                String tag = mention.tag();
                if (tag != null) action.complete().reply(tag).complete().delete().queueAfter(250, TimeUnit.MILLISECONDS);
                else action.queue();
                event.reply("This command works better with a space in front of the slash").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
            } else {
                event.reply("The first argument has to be a message channel").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
            }
        } else if (member == null) {
            event.reply("This action cannot be performed here").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
        } else if (channel == null) {
            event.reply("No target channel defined").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
        } else event.reply("Found no content to send").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
    }
}
