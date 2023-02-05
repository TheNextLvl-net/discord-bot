package net.nonswag.tnl.bot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.nonswag.tnl.bot.api.command.Command;
import net.nonswag.tnl.bot.api.command.CommandListener;
import net.nonswag.tnl.bot.api.embed.Embed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

public class SayCommand extends CommandListener {
    @Getter
    private static final SayCommand instance = new SayCommand();
    private static final Logger logger = LoggerFactory.getLogger(SayCommand.class);

    private SayCommand() {
        super("say [message]", "Let the bot say something");
        getSlashCommand().editCommand().addOption(OptionType.STRING, "message", "message to send", true).queue();
    }

    @Override
    public void onCommand(JDA jda, @Nullable Guild guild, @Nullable Member member, MessageChannelUnion channel, Message message, Command command) {
        new Thread(() -> {
            if (!hasPermission(member, channel, Permission.MESSAGE_MANAGE)) {
                channel.sendMessage("You have no rights (MESSAGE_MANAGE)").complete().delete().queueAfter(10, TimeUnit.SECONDS);
                return;
            }
            assert member != null;
            if (!command.getArguments().isEmpty() || !message.getAttachments().isEmpty()) {
                String say;
                if (command.getArguments().isEmpty()) say = " ";
                else say = command.getFullCommand().substring(5);
                MessageCreateAction action;
                boolean embed = false;
                if (say.startsWith("embed:")) {
                    say = say.substring(6, Math.min(say.length(), 256));
                    embed = true;
                }
                Message ref = message.getReferencedMessage();
                if (ref != null) {
                    if (embed) action = ref.replyEmbeds(Embed.create(say, member));
                    else action = ref.reply(say);
                } else {
                    if (embed) action = channel.sendMessageEmbeds(Embed.create(say, member));
                    else action = channel.sendMessage(say);
                }
                var attachments = message.getAttachments();
                if (!attachments.isEmpty()) {
                    channel.sendTyping().queue();
                    for (int i = 0; i < attachments.size(); i++) {
                        var attachment = attachments.get(i);
                        int finalI = i;
                        attachment.getProxy().download().thenAccept(result -> {
                            action.addFiles(FileUpload.fromData(result, attachment.getFileName()));
                            if (finalI == attachments.size() - 1) action.queue();
                        });
                    }
                } else action.queue();
            } else channel.sendMessage("Found no content to send").complete().delete().queueAfter(10, TimeUnit.SECONDS);
        }).start();
        message.delete().queueAfter(250, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onSlashCommand(Command command, SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (!hasPermission(member, event.getChannel(), Permission.MESSAGE_MANAGE)) {
            event.reply("You have no rights (MESSAGE_MANAGE)").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
            return;
        }
        assert member != null;
        OptionMapping message = event.getOption("message");
        if (message != null) {
            String say = message.getAsString();
            MessageCreateAction action;
            if (say.startsWith("embed:")) {
                say = say.substring(6, Math.min(say.length(), 256));
                action = event.getChannel().sendMessageEmbeds(Embed.create(say, member));
            } else action = event.getChannel().sendMessage(say);
            action.queue();
            event.reply("This command works better with a space in front of the slash").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
        } else event.reply("Found no content to send").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
    }
}
