package net.nonswag.tnl.bot.api.permission;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PermissionHelper {

    public static boolean hasPermission(@Nullable Member member, MessageChannelUnion messageChannel, Permission... permissions) {
        if (member == null) return false;
        if (member.isOwner() || member.getUser().isBot() || member.getUser().isSystem()) return true;
        if (messageChannel instanceof PrivateChannel) return true;
        if(messageChannel instanceof NewsChannel) return true;
        if (member.hasPermission(permissions)) return true;
        for (Role role : member.getRoles()) if (role.hasPermission(permissions)) return true;
        if (!(messageChannel instanceof TextChannel channel)) return false;
        PermissionOverride override = channel.getPermissionOverride(member);
        if (override != null && hasPermission(member, override, permissions)) return true;
        for (Role role : member.getRoles()) {
            override = channel.getPermissionOverride(role);
            if (override != null && hasPermission(member, override, permissions)) return true;
        }
        return false;
    }

    private static boolean hasPermission(Member member, PermissionOverride override, Permission... permissions) {
        for (Permission permission : permissions) if (override.getAllowed().contains(permission)) return true;
        return false;
    }
}
