package net.thenextlvl.bot;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.Event;
import net.thenextlvl.bot.command.CommandRegistry;
import net.thenextlvl.bot.command.ForkCommand;
import reactor.core.publisher.Mono;

public class Bot {
    public static final long GUILD_ID = Long.parseLong(System.getenv("GUILD_ID"));

    private static final CommandRegistry commandRegistry = new CommandRegistry();

    static void main() {
        System.out.printf("Starting bot... %s%n", GUILD_ID);
        DiscordClient.create(System.getenv("DISCORD_TOKEN")).withGateway(gateway -> {
            commandRegistry.register(new ForkCommand());

            var registerCommands = commandRegistry.registerCommands(gateway);
            var registerDispatcher = commandRegistry.registerDispatcher(gateway);

            var debug = gateway.on(Event.class, event -> {
                System.out.println(event.getClass().getSimpleName());
                return Mono.empty();
            }).then();
            return registerCommands.then(registerDispatcher.then(debug));
        }).block();
    }
}
