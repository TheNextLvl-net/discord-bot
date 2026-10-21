# DiscordBot

A Discord slash-command bot running on [Cloudflare Workers](https://workers.cloudflare.com/)
via Discord's HTTP interactions endpoint (no gateway connection needed).

## Setup

````sh
bun install

# Store the "Public Key" from the Discord developer portal as a Worker secret
bunx wrangler secret put DISCORD_PUBLIC_KEY

# Deploy the worker
bun run deploy
````

Then, in the [Discord developer portal](https://discord.com/developers/applications), set
**General Information → Interactions Endpoint URL** to your worker URL
(e.g. `https://discord-bot.<your-subdomain>.workers.dev`).

## Registering commands

Commands are registered to a single guild. Run this whenever commands change:

````sh
cp .env.example .env   # fill in DISCORD_TOKEN and GUILD_ID
bun run register
````

## Local development

````sh
cp .dev.vars.example .dev.vars   # fill in DISCORD_PUBLIC_KEY
bun run dev
````

## Invite bot

https://discord.com/oauth2/authorize?client_id=1379182822941528094&permissions=274877908992&integration_type=0&scope=bot+applications.commands
