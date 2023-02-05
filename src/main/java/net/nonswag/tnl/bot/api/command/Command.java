package net.nonswag.tnl.bot.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Command {
    private String command;
    private final List<String> arguments;

    public String getFullCommand() {
        return "/" + getCommand() + " " + String.join(" ", getArguments());
    }
}
