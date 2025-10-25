package net.thenextlvl.bot.command;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import net.thenextlvl.bot.Bot;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    public Mono<Void> registerCommands(GatewayDiscordClient gateway) {
        return gateway.getRestClient().getApplicationId().publishOn(Schedulers.boundedElastic()).doOnSuccess(applicationId -> {
            var service = gateway.getRestClient().getApplicationService();
            for (var command : commands.values()) {
                service.createGuildApplicationCommand(applicationId, Bot.GUILD_ID, command.create()).subscribe();
            }
        }).then();
    }

    public Mono<Void> registerDispatcher(GatewayDiscordClient gateway) {
        return gateway.getEventDispatcher().on(ChatInputInteractionEvent.class, event -> {
            var command = commands.get(event.getCommandName());
            if (command == null) return Mono.empty();
            return command.execute(event);
        }).then();
    }
}
