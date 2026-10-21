import type { APIApplication } from "discord-api-types/v10";
import { commands } from "../src/commands/index.ts";

const token = process.env.DISCORD_TOKEN;
const guildId = process.env.GUILD_ID;
if (!token || !guildId) throw new Error("DISCORD_TOKEN and GUILD_ID must be set");

const api = "https://discord.com/api/v10";
const headers = { "Authorization": `Bot ${token}`, "Content-Type": "application/json" };

const appResponse = await fetch(`${api}/applications/@me`, { headers });
if (!appResponse.ok) throw new Error(`Failed to fetch application: ${appResponse.status} ${await appResponse.text()}`);
const application = await appResponse.json() as APIApplication;

const response = await fetch(`${api}/applications/${application.id}/guilds/${guildId}/commands`, {
    method: "PUT",
    headers,
    body: JSON.stringify([...commands.values()].map(command => command.data)),
});
if (!response.ok) throw new Error(`Failed to register commands: ${response.status} ${await response.text()}`);

console.log(`Registered ${commands.size} commands in guild ${guildId}`);
