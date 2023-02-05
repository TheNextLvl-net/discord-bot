package net.nonswag.tnl.bot.listener;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.nonswag.core.api.message.Message;
import net.nonswag.core.api.message.Placeholder;
import net.nonswag.tnl.bot.api.simple.Channel;
import net.nonswag.tnl.bot.utils.Messages;

public class MemberListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (Channel.SERVER_ENTRANCE.getChannel() == null) return;
        String message = Message.format(Messages.MEMBER_JOINED_SERVER.message(), new Placeholder("user", event.getUser().getAsMention()));
        Channel.SERVER_ENTRANCE.getChannel().sendMessage(message).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        if (Channel.SERVER_EXIT.getChannel() == null) return;
        String message = Message.format(Messages.MEMBER_LEFT_SERVER.message(), new Placeholder("user", event.getUser().getAsTag()));
        Channel.SERVER_EXIT.getChannel().sendMessage(message).queue();
    }

    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event) {
        if (Channel.BOOSTS.getChannel() == null) return;
        String message = Message.format(Messages.MEMBER_BOOSTED_SERVER.message(), new Placeholder("user", event.getUser().getAsMention()));
        Channel.BOOSTS.getChannel().sendMessage(message).queue();
    }
}
