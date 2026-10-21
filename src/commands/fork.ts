import { InteractionResponseType } from "discord-api-types/v10";
import { Color, type Command } from "./command.ts";

export const fork: Command = {
    data: {
        name: "fork",
        description: "Get information about using forks of Paper/Folia",
    },
    execute() {
        return {
            type: InteractionResponseType.ChannelMessageWithSource,
            data: {
                embeds: [{
                    title: ":warning: Fork Usage Notice",
                    description: "You are using a fork of Paper/Folia. Please reproduce your issue on Paper or Folia, " +
                        "as we can't guarantee stability or functionality for every arbitrary fork.",
                    fields: [{
                        name: "Official Resources",
                        value: "**Paper:** https://papermc.io/software/paper/\n" +
                            "**Folia:** https://papermc.io/software/folia/",
                        inline: false,
                    }],
                    footer: { text: "If the issue persists, please provide reproduction steps." },
                    color: Color.MOON_YELLOW,
                }],
            },
        };
    },
};
