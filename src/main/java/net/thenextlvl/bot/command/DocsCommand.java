package net.thenextlvl.bot.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

public class DocsCommand implements Command {
    private final Map<String, String> docsLinks = Map.ofEntries(
            Map.entry("Characters", "https://thenextlvl.net/docs/characters"),
            Map.entry("Cinematics", "https://thenextlvl.net/docs/cinematics"),
            Map.entry("Commander", "https://thenextlvl.net/docs/commander"),
            Map.entry("CreativeUtilities", "https://thenextlvl.net/docs/creativeutilities"),
            Map.entry("Economist", "https://thenextlvl.net/docs/economist"),
            Map.entry("goPaintAdvanced", "https://thenextlvl.net/docs/gopaintadvanced"),
            Map.entry("Holograms", "https://thenextlvl.net/docs/holograms"),
            Map.entry("PerWorlds", "https://thenextlvl.net/docs/perworlds"),
            Map.entry("Portals", "https://thenextlvl.net/docs/portals"),
            Map.entry("Protect", "https://thenextlvl.net/docs/protect"),
            Map.entry("RedProtect", "https://thenextlvl.net/docs/redprotect"),
            Map.entry("ServiceIO", "https://thenextlvl.net/docs/serviceio"),
            Map.entry("Tweaks", "https://thenextlvl.net/docs/tweaks"),
            Map.entry("Worlds", "https://thenextlvl.net/docs/worlds")
    );

    @Override
    public String getName() {
        return "docs";
    }

    @Override
    public String getDescription() {
        return "Get a docs link to a plugin";
    }

    @Override
    public ImmutableApplicationCommandRequest.Builder create() {
        return Command.super.create().addOption(ApplicationCommandOptionData.builder()
                .name("plugin")
                .description("The name of the plugin")
                .type(ApplicationCommandOption.Type.STRING.getValue())
                .required(true)
                .choices(docsLinks.entrySet().stream()
                        .map(s -> ApplicationCommandOptionChoiceData.builder()
                                .name(s.getKey())
                                .value(s.getValue())
                                .build())
                        .toList())
                .build());
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        var plugin = event.getOption("plugin")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString);
        return event.reply(plugin.orElse("null"));
    }
}
