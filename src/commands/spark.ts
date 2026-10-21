import { InteractionResponseType } from "discord-api-types/v10";
import { Color, type Command } from "./command.ts";

export const spark: Command = {
    data: {
        name: "spark",
        description: "Request a spark report",
    },
    execute() {
        return {
            type: InteractionResponseType.ChannelMessageWithSource,
            data: {
                embeds: [{
                    title: "Please provide a spark report!",
                    thumbnail: { url: "https://spark.lucko.me/assets/logo-512.png" },
                    description: "Run the command `/spark profiler start --timeout 11` to generate a spark report.\n" +
                        "Copy and paste the URL here, so we can review your setup and configurations.",
                    fields: [{
                        name: "Spark Download",
                        value: "**Paper:** Spark is already included in Paper\n" +
                            "**Folia:** https://ci.lucko.me/job/spark-extra-platforms/",
                        inline: false,
                    }],
                    footer: { text: "Support will be continued when you provide the requested information." },
                    color: Color.MOON_YELLOW,
                }],
            },
        };
    },
};
