import {
    InteractionResponseType,
    InteractionType,
    type APIInteraction,
} from "discord-api-types/v10";
import { isChatInputApplicationCommandInteraction } from "discord-api-types/utils/v10";
import { commands } from "./commands/index.ts";
import { verifyDiscordRequest } from "./verify.ts";

interface Env {
    DISCORD_PUBLIC_KEY: string;
}

export default {
    async fetch(request, env): Promise<Response> {
        if (request.method !== "POST") return new Response("Not Found", { status: 404 });

        const body = await request.text();
        if (!await verifyDiscordRequest(request, body, env.DISCORD_PUBLIC_KEY)) {
            return new Response("Bad request signature", { status: 401 });
        }

        const interaction = JSON.parse(body) as APIInteraction;

        if (interaction.type === InteractionType.Ping) {
            return Response.json({ type: InteractionResponseType.Pong });
        }

        if (interaction.type === InteractionType.ApplicationCommand
            && isChatInputApplicationCommandInteraction(interaction)) {
            const command = commands.get(interaction.data.name);
            if (command) return Response.json(await command.execute(interaction));
        }

        return new Response("Unknown interaction", { status: 400 });
    },
} satisfies ExportedHandler<Env>;
