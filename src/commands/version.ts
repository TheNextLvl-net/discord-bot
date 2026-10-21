import { InteractionResponseType } from "discord-api-types/v10";
import { Color, type Command } from "./command.ts";

export const version: Command = {
    data: {
        name: "version",
        description: "Request the server and plugin version",
    },
    execute() {
        return {
            type: InteractionResponseType.ChannelMessageWithSource,
            data: {
                embeds: [{
                    title: "Provide the server and plugin version",
                    description: "Please provide the unmodified output of `/version`, and `/version <plugin>`.\n" +
                        "You can copy the message from the terminal, or the chat by left-clicking on it.\n" +
                        "A screenshot of the output is also acceptable.\n",
                    footer: { text: "Support will be continued when you provide the requested information." },
                    color: Color.SEA_GREEN,
                }],
            },
        };
    },
};
