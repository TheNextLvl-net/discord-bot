package net.thenextlvl.bot.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public final class SparkCommand implements Command {
    @Override
    public String getName() {
        return "spark";
    }

    @Override
    public String getDescription() {
        return "Request a spark report";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        var embed = EmbedCreateSpec.builder()
                .title("Please provide a spark report!")
                .thumbnail("https://spark.lucko.me/assets/logo-512.png")
                .description("Run the command `/spark profiler start --timeout 11` to generate a spark report.\n" +
                        "Copy and paste the URL here, so we can review your setup and configurations.")
                .addField("Spark Download", """
                        **Paper:** Spark is already included in Paper
                        **Folia:** https://ci.lucko.me/job/spark-extra-platforms/""", false)
                .footer("Support will be continued when you provide the requested information.", null)
                .color(Color.MOON_YELLOW)
                .build();

        var replySpec = InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(embed)
                .build();

        return event.reply(replySpec);
    }
}
