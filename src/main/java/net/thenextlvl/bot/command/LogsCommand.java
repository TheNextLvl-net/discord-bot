package net.thenextlvl.bot.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public final class LogsCommand implements Command {
    @Override
    public String getName() {
        return "logs";
    }

    @Override
    public String getDescription() {
        return "Request the latest server logs";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        var embed = EmbedCreateSpec.builder()
                .title(":page_facing_up: Provide the latest server logs")
                .description("""
                        The latest logs of your server are located in `logs/latest.log`.
                        Copy and paste the entire contents of the file to [mclo.gs](https://mclo.gs/) and send the URL here.
                        """)
                .color(Color.SEA_GREEN)
                .build();

        var replySpec = InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(embed)
                .build();

        return event.reply(replySpec);
    }
}
