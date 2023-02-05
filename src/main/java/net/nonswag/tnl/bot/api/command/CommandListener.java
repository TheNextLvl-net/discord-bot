package net.nonswag.tnl.bot.api.command;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.nonswag.tnl.bot.Bot;
import net.nonswag.tnl.bot.api.permission.PermissionHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public abstract class CommandListener {
    @Getter
    private static final List<net.dv8tion.jda.api.interactions.commands.Command> commands = Bot.getJda().retrieveCommands().complete();

    private final String command;
    private final String commandDescription;
    private final String description;
    private final net.dv8tion.jda.api.interactions.commands.Command slashCommand;

    public CommandListener(String command, String description) {
        this.command = command.split(" ")[0];
        this.commandDescription = command;
        this.description = description;
        for (net.dv8tion.jda.api.interactions.commands.Command value : getCommands()) {
            if (!value.getName().equalsIgnoreCase(getCommand())) continue;
            this.slashCommand = value;
            return;
        }
        this.slashCommand = Bot.getJda().upsertCommand(getCommand(), getDescription()).complete();
    }

    public abstract void onCommand(JDA jda, @Nullable Guild guild, @Nullable Member member, MessageChannelUnion channel, Message message, Command command);

    public void onSlashCommand(Command command, SlashCommandInteractionEvent event) {
        event.reply("There is no slash command action defined now").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean hasPermission(@Nullable Member member, MessageChannelUnion channel, Permission... permissions) {
        return PermissionHelper.hasPermission(member, channel, permissions);
    }
}
