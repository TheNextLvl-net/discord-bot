package net.thenextlvl.bot.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class VersionCommand implements Command {
    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescription() {
        return "Request the server and plugin version";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        var embed = EmbedCreateSpec.builder()
                .title("Provide the server and plugin version")
                .description("""
                        Please provide the unmodified output of `/version`, and `/version <plugin>`.
                        You can copy the message from the terminal, or the chat by left-clicking on it.
                        A screenshot of the output is also acceptable.
                        """)
                .footer("Support will be continued when you provide the requested information.", null)
                .color(Color.SEA_GREEN)
                .build();

        var replySpec = InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(embed)
                .build();

        return event.reply(replySpec);
    }
}
