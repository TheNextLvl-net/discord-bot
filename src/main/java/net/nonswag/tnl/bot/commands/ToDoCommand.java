package net.nonswag.tnl.bot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import net.nonswag.core.api.object.Pair;
import net.nonswag.tnl.bot.api.command.Command;
import net.nonswag.tnl.bot.api.command.CommandListener;
import net.nonswag.tnl.bot.api.simple.Channel;
import net.nonswag.tnl.bot.api.simple.Emoji;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ToDoCommand extends CommandListener {
    @Getter
    private static final ToDoCommand instance = new ToDoCommand();

    private ToDoCommand() {
        super("todo [category]; [action, ...] | ... ", "create a todo list");
        getSlashCommand().editCommand().addOptions(new OptionData(OptionType.STRING, "todo", "[category]; [action, ...] | ...", true)).queue();
    }

    @Override
    public void onCommand(JDA jda, @Nullable Guild guild, @Nullable Member member, MessageChannelUnion channel, Message msg, Command command) {
        new Thread(() -> {
            if (Channel.TODO.getChannel() != null && Channel.TODO.getChannel().equals(channel)) {
                if (command.getArguments().isEmpty()) {
                    channel.sendMessage("/todo [category]; [action, ...] | ... ").complete().delete().queueAfter(10, TimeUnit.SECONDS);
                } else {
                    List<Pair<String, String[]>> categories = new ArrayList<>();
                    String arguments = String.join(" ", command.getArguments());
                    for (String s : arguments.split(" \\| ")) {
                        List<String> substrings = Arrays.asList(s.split("; "));
                        categories.add(new Pair<>(substrings.get(0), String.join("; ", substrings.subList(1, substrings.size())).split(", ")));
                    }
                    StringBuilder sb = new StringBuilder();
                    for (Pair<String, String[]> category : categories) {
                        sb.append(category.getKey()).append(":\n");
                        if (category.getValue() != null) {
                            for (String todo : category.getValue()) sb.append("  - ").append(todo).append("\n");
                        }
                    }
                    String say = "```yaml\n" + sb + "\n```";
                    Message message = channel.sendMessage(say).addFiles(FileUpload.fromData(arguments.getBytes(StandardCharsets.UTF_8), "raw.txt")).complete();
                    if (Emoji.YES.getEmoji() != null) message.addReaction(Emoji.YES.getEmoji()).queue();
                }
            } else {
                assert Channel.TODO.getChannel() != null;
                channel.sendMessage("This action cannot be performed here (use " + Channel.TODO.getChannel().getAsMention() + ")").complete().delete().queueAfter(10, TimeUnit.SECONDS);
            }
        }).start();
        msg.delete().queueAfter(250, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onSlashCommand(Command command, SlashCommandInteractionEvent event) {
        if (event.isFromGuild() && Channel.TODO.getChannel() != null && Channel.TODO.getChannel().getIdLong() == event.getChannel().getIdLong()) {
            if (!command.getArguments().isEmpty()) {
                List<Pair<String, String[]>> categories = new ArrayList<>();
                String arguments = String.join(" ", command.getArguments());
                for (String s : arguments.split(" \\| ")) {
                    List<String> substrings = Arrays.asList(s.split("; "));
                    categories.add(new Pair<>(substrings.get(0), String.join("; ", substrings.subList(1, substrings.size())).split(", ")));
                }
                StringBuilder sb = new StringBuilder();
                for (Pair<String, String[]> category : categories) {
                    sb.append(category.getKey()).append(":\n");
                    if (category.getValue() != null) {
                        for (String todo : category.getValue()) sb.append("  - ").append(todo).append("\n");
                    }
                }
                Message action = event.getChannel().sendMessage("```yaml\n" + sb + "\n```").
                        addFiles(FileUpload.fromData(arguments.getBytes(StandardCharsets.UTF_8), "raw.txt")).complete();
                if (Emoji.YES.getEmoji() != null) action.addReaction(Emoji.YES.getEmoji()).queue();
                event.reply("Todo list was written").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
            } else {
                event.reply("/todo [category]; [action, ...] | ... ").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
            }
        } else {
            assert Channel.TODO.getChannel() != null;
            event.reply("This action cannot be performed here (use " + Channel.TODO.getChannel().getAsMention() + ")").complete().deleteOriginal().queueAfter(10, TimeUnit.SECONDS);
        }
    }
}
