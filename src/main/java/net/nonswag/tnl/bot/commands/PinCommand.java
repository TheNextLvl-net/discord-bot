package net.nonswag.tnl.bot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.nonswag.tnl.bot.api.command.Command;
import net.nonswag.tnl.bot.api.command.CommandListener;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

public class PinCommand extends CommandListener {
    @Getter
    private static final PinCommand instance = new PinCommand();

    private PinCommand() {
        super("pin", "Pin the referenced message");
    }

    @Override
    public void onCommand(JDA jda, @Nullable Guild guild, @Nullable Member member, MessageChannelUnion channel, Message message, Command command) {
        message.delete().queueAfter(250, TimeUnit.MILLISECONDS);
        if (hasPermission(member, channel, Permission.MESSAGE_MANAGE)) {
            if (message.getReferencedMessage() != null) {
                if (!message.getReferencedMessage().isPinned()) {
                    message.getReferencedMessage().pin().queue();
                    message.getReferencedMessage().reply("Pinned this message").complete().delete().queueAfter(10, TimeUnit.SECONDS);
                } else {
                    message.getReferencedMessage().reply("This message is already pinned").complete().delete().queueAfter(10, TimeUnit.SECONDS);
                }
            } else {
                channel.sendMessage("Reply to the message you want to pin").complete().delete().queueAfter(10, TimeUnit.SECONDS);
            }
        } else {
            channel.sendMessage("You have no rights (MESSAGE_MANAGE)").complete().delete().queueAfter(10, TimeUnit.SECONDS);
        }
    }

    @Override
    public void onSlashCommand(Command command, SlashCommandInteractionEvent event) {
        event.reply("Due to discord restrictions this command doesn't work\n" +
                "Execute this command again but add a space in front of the slash").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
    }
}
