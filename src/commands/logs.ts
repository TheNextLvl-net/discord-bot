import { InteractionResponseType } from "discord-api-types/v10";
import { Color, type Command } from "./command.ts";

export const logs: Command = {
    data: {
        name: "logs",
        description: "Request the latest server logs",
    },
    execute() {
        return {
            type: InteractionResponseType.ChannelMessageWithSource,
            data: {
                embeds: [{
                    title: ":page_facing_up: Provide the latest server logs",
                    description: "The latest logs of your server are located in `logs/latest.log`.\n" +
                        "Copy and paste the entire contents of the file to [mclo.gs](https://mclo.gs/) and send the URL here.\n",
                    color: Color.SEA_GREEN,
                }],
            },
        };
    },
};
