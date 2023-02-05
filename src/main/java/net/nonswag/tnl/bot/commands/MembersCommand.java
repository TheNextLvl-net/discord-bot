package net.nonswag.tnl.bot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.nonswag.tnl.bot.api.command.Command;
import net.nonswag.tnl.bot.api.command.CommandListener;
import net.nonswag.tnl.bot.api.embed.Embed;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

public class MembersCommand extends CommandListener {
    @Getter
    private static final MembersCommand instance = new MembersCommand();
    private static final String FAILED = "This action cannot be performed here";

    private MembersCommand() {
        super("members", "The number of members on the discord server");
    }

    @Override
    public void onCommand(JDA jda, @Nullable Guild guild, @Nullable Member member, MessageChannelUnion channel, Message message, Command command) {
        message.delete().queueAfter(250, TimeUnit.MILLISECONDS);
        if (guild != null && member != null) {
            channel.sendMessageEmbeds(embed(member)).complete().delete().queueAfter(25, TimeUnit.SECONDS);
        } else channel.sendMessage(FAILED).complete().delete().queueAfter(10, TimeUnit.SECONDS);
    }

    @Override
    public void onSlashCommand(Command command, SlashCommandInteractionEvent event) {
        if (event.getMember() == null) event.reply(FAILED).complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
        else event.replyEmbeds(embed(event.getMember())).complete().deleteOriginal().queueAfter(25, TimeUnit.SECONDS);
    }

    private MessageEmbed embed(Member member) {
        return Embed.create("Members: " + member.getGuild().getMemberCount(), member);
    }
}
