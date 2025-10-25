package net.thenextlvl.bot.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import reactor.core.publisher.Mono;

public interface Command {
    String getName();

    String getDescription();

    default ImmutableApplicationCommandRequest.Builder create() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description(getDescription());
    }

    Mono<Void> execute(ChatInputInteractionEvent event);
}
