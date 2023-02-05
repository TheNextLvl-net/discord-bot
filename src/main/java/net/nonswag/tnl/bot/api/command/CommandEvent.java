package net.nonswag.tnl.bot.api.command;

import lombok.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class CommandEvent {
    private final JDA jda;
    private final User executor;
    @Nullable
    private final Guild guild;
    @Nullable
    private final Member member;
    private final MessageChannelUnion channel;
    private final Message message;
    private final Command command;

    private boolean cancelled = false;
}
