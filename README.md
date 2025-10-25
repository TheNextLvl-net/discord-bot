# DiscordBot

````sh
export DISCORD_TOKEN=ABCDE.ABCDE.ABCDE_ABCDE
export GUILD_ID=1234567890
docker build -t thenextlvl:discord-bot .
docker run -e DISCORD_TOKEN -e GUILD_ID --name discord-bot -it thenextlvl:discord-bot
````

## Invite bot

https://discord.com/oauth2/authorize?client_id=1379182822941528094&permissions=274877908992&integration_type=0&scope=bot+applications.commands