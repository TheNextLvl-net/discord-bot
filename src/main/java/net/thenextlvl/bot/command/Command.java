package net.thenextlvl.bot.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

public interface Command {
    String getName();

    String getDescription();

    default ApplicationCommandRequest create() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description(getDescription())
                .build();
    }

    Mono<Void> execute(ChatInputInteractionEvent event);
}
