package net.thenextlvl.bot;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.Event;
import net.thenextlvl.bot.command.CommandRegistry;
import net.thenextlvl.bot.command.ForkCommand;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

public class Bot {
    public static final long GUILD_ID = Long.parseLong(System.getenv("GUILD_ID"));

    private static final String TOKEN = System.getenv("DISCORD_TOKEN");
    private static final CommandRegistry commandRegistry = new CommandRegistry();
    
    private static final Set<Mono<Void>> listeners = new HashSet<>();

    static void main() {
        DiscordClient.create(TOKEN).withGateway(gateway -> {
            commandRegistry.register(new ForkCommand());

            commandRegistry.registerCommands(gateway);
            commandRegistry.registerDispatcher(gateway);

            registerListener(gateway.on(Event.class, event -> {
                System.out.println(event.getClass().getSimpleName());
                return Mono.empty();
            }).then());
            return Mono.when(listeners.stream().toList());
        }).block();
    }
    
    public static void registerListener(Mono<Void> listener) {
        listeners.add(listener);
    }
}
