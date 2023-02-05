package net.nonswag.tnl.bot.listener;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.nonswag.tnl.bot.api.simple.Channel;

public class VoiceListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (event.getChannelLeft() != null && event.getChannelLeft().getName().equals(event.getEntity().getUser().getAsTag())) {
            event.getChannelLeft().delete().queue();
        }
        if (event.getChannelJoined() != null && event.getChannelJoined().getIdLong() == Channel.CREATE_VOICE_CHANNEL.getId()) {
            if (event.getChannelJoined().getParentCategory() != null) {
                var voice = event.getChannelJoined().getParentCategory().createVoiceChannel(event.getEntity().getUser().getAsTag()).complete();
                event.getGuild().moveVoiceMember(event.getEntity(), voice).queue();
            } else event.getGuild().kickVoiceMember(event.getEntity()).queue();
        }
    }
}
