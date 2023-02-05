package net.nonswag.tnl.bot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.nonswag.tnl.bot.Bot;
import net.nonswag.tnl.bot.api.command.Command;
import net.nonswag.tnl.bot.api.command.CommandListener;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

public class HelpCommand extends CommandListener {
    @Getter
    private static final HelpCommand instance = new HelpCommand();

    private HelpCommand() {
        super("help", "Get some help");
    }

    @Override
    public void onCommand(JDA jda, @Nullable Guild guild, @Nullable Member member, MessageChannelUnion channel, Message message, Command command) {
        message.delete().queueAfter(250, TimeUnit.MILLISECONDS);
        channel.sendMessageEmbeds(embed(guild)).complete().delete().queueAfter(25, TimeUnit.SECONDS);
    }

    @Override
    public void onSlashCommand(Command command, SlashCommandInteractionEvent event) {
        Guild guild = event.isFromGuild() ? event.getGuild() : null;
        event.replyEmbeds(embed(guild)).complete().deleteOriginal().queueAfter(25, TimeUnit.SECONDS);
    }

    private MessageEmbed embed(@Nullable Guild guild) {
        StringBuilder sb = new StringBuilder();
        for (CommandListener listener : Bot.COMMAND_LISTENERS) {
            if (listener.getCommand().equals("help")) continue;
            sb.append("» `/").append(listener.getCommandDescription()).append("` - _").append(listener.getDescription()).append("_\n");
        }
        MessageEmbed.Field field = new MessageEmbed.Field("- Discord Help -", sb.toString(), true, false);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(guild != null ? guild.getSelfMember().getColor() : null);
        embed.addField(field);
        return embed.build();
    }
}
