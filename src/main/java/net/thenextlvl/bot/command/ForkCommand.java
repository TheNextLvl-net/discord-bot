package net.thenextlvl.bot.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class ForkCommand implements Command {
    @Override
    public String getName() {
        return "fork";
    }

    @Override
    public String getDescription() {
        return "Get information about using forks of Paper/Folia";
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        var embed = EmbedCreateSpec.builder()
                .title(":warning: Fork Usage Notice")
                .description("You are using a fork of Paper/Folia. Please reproduce your issue on Paper or Folia, " +
                        "as we can't guarantee stability or functionality for every arbitrary fork.")
                .addField("Official Resources", """
                        **Paper:** https://papermc.io/software/paper/
                        **Folia:** https://papermc.io/software/folia/""", false)
                .footer("If the issue persists, please provide reproduction steps.", null)
                .color(Color.MOON_YELLOW)
                .build();

        var replySpec = InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(embed)
                .build();

        return event.reply(replySpec);
    }
}
