package net.nonswag.tnl.bot.api.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.nonswag.core.api.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Embed {
    public static MessageEmbed create(String content, Member member) {
        return new EmbedBuilder().setAuthor(member.getUser().getName(), "https://www.thenextlvl.net", member.getEffectiveAvatarUrl()).
                setColor(member.getGuild().getSelfMember().getColor()).setDescription(content).build();
    }
}
