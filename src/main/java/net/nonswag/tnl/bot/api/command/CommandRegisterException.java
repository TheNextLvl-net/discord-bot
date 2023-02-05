package net.nonswag.tnl.bot.api.command;

public class CommandRegisterException extends Exception {
    public CommandRegisterException() {
        this("An error has occurred while registering command");
    }

    public CommandRegisterException(String message) {
        super(message);
    }
}
