import { InteractionResponseType } from "discord-api-types/v10";
import { Color, type Command } from "./command.ts";

export const language: Command = {
    data: {
        name: "language",
        description: "Get information about the language of this server",
    },
    execute() {
        return {
            type: InteractionResponseType.ChannelMessageWithSource,
            data: {
                embeds: [{
                    title: "This is an english speaking discord server",
                    description: "Please use english for all communication to ensure everyone can understand you.\n" +
                        "If you are not fluent in english, you can use a translation tool like [DeepL](https://www.deepl.com/).\n",
                    color: Color.SEA_GREEN,
                }],
            },
        };
    },
};
