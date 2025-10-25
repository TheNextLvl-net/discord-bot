package net.thenextlvl.bot.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
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
                .title("⚠️ Fork Usage Notice")
                .description("You are using a fork of Paper/Folia. Please reproduce your issue on Paper or Folia, as we can't guarantee stability or functionality for every arbitrary fork.")
                .addField("Official Resources", """
                        **Paper:** https://papermc.io/software/paper/
                        **Folia:** https://papermc.io/software/folia/""", false)
                .addField("Why This Matters", """
                        Forks may have different implementations, bugs, or features that are not present in the original projects. \
                        To ensure we can properly help you, please test your issue on the official Paper or Folia builds first.""", false)
                .footer("If the issue persists on official builds, please provide reproduction steps.", null)
                .build();

        var replySpec = InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(embed)
                .build();

        return event.reply(replySpec);
    }
}
