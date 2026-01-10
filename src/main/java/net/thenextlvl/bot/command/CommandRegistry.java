package net.thenextlvl.bot.command;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import net.thenextlvl.bot.Bot;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public final class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    public void registerCommands(GatewayDiscordClient gateway) {
        Bot.registerListener(gateway.getRestClient().getApplicationId().flatMap(applicationId -> {
            var service = gateway.getRestClient().getApplicationService();
            return Mono.when(commands.values().stream()
                    .map(command -> command.create().build())
                    .map(command -> service.createGuildApplicationCommand(applicationId, Bot.GUILD_ID, command))
                    .toList());
        }).then());
    }

    public void registerDispatcher(GatewayDiscordClient gateway) {
        Bot.registerListener(gateway.getEventDispatcher().on(ChatInputInteractionEvent.class, event -> {
            var command = commands.get(event.getCommandName());
            if (command == null) return Mono.empty();
            return command.execute(event);
        }).then());
    }
}
