package net.thenextlvl.bot.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public final class LanguageCommand implements Command {
    @Override
    public String getName() {
        return "language";
    }

    @Override
    public String getDescription() {
        return "Get information about the language of this server";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        var embed = EmbedCreateSpec.builder()
                .title("This is an english speaking discord server")
                .description("""
                        Please use english for all communication to ensure everyone can understand you.
                        If you are not fluent in english, you can use a translation tool like [DeepL](https://www.deepl.com/).
                        """)
                .color(Color.SEA_GREEN)
                .build();

        var replySpec = InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(embed)
                .build();

        return event.reply(replySpec);
    }
}
