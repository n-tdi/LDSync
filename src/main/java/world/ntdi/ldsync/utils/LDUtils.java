package world.ntdi.ldsync.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import world.ntdi.ldsync.LDSync;

import java.util.ArrayList;
import java.util.List;

public class LDUtils {
    public static List<Role> getRoles() {
        Guild g = LDSync.jda.getGuildById(LDSync.getDiscordServerId());
        return g.getRoles();
    }

    public static Member getMember(String name) {
        Guild g = LDSync.jda.getGuildById(LDSync.getDiscordServerId());
        g.getMemberByTag(name);
        return g.getMemberByTag(name);
    }

    public static boolean hasRank(Player p) {
        return LDSync.permissions.getPrimaryGroup(p) != null;
    }

    public static boolean alreadyHasUpdatedRole(String name, String rank) {
        Member member = getMember(name);
        List<Role> hasRoles = member.getRoles();
        if (hasRoles.isEmpty()) {
            return false;
        }
        for(Role hr : hasRoles) {
            if(hr.getName().equals(rank)) {
                return true;
            }
        }
        return false;
    }

    public static Role findRoleFromName(String name) {
        List<Role> roles = getRoles();
        for(Role r : roles) {
            if(r.getName().equalsIgnoreCase(name)) {
                return r;
            }
        }
        return null;
    }

    public static Role createRoleFromName(String name) {
        Guild g = LDSync.jda.getGuildById(LDSync.getDiscordServerId());
        try {
            g.createRole().setName(name).setHoisted(true).setMentionable(false).queue();
            return findRoleFromName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Role> findHigherRoles(Role baseRole) {
        List<Role> higherRoles = new ArrayList<>();
        for (Role r : getRoles()) {
            if (r.getPosition() > baseRole.getPosition()) {
                higherRoles.add(r);
            }
        }
        return higherRoles;
    }

    public static List<Role> rolesThatHas(Member member, List<Role> l) {
        List<Role> roles = new ArrayList<>();
        List<Role> hasRoles = member.getRoles();
        for(Role r : l) {
            if(hasRoles.contains(r)) {
                roles.add(r);
            }
        }
        return roles;
    }

    public static void removeRoles(Guild g, Member member, List<Role> rolesToRemove) {
        for (Role r : rolesToRemove) {
            g.removeRoleFromMember(member, r).queue();
        }
    }

    public static void Sync(CommandSender sender, Player p, String name, String rankName) {
        if(!hasRank(p)) {
            sender.sendMessage(ChatColor.RED + "You do not have a rank!");
            return;
        }
        if(alreadyHasUpdatedRole(name, rankName)) {
            sender.sendMessage(ChatColor.RED + "Player already has synced role!");
            return;
        }
        Role role = (findRoleFromName(rankName) != null) ? findRoleFromName(rankName) : createRoleFromName(rankName);
        if(role == null) {
            sender.sendMessage(ChatColor.RED + "Failed to find role!");
            return;
        }
        Member member = getMember(name);
        if(member == null) {
            sender.sendMessage(ChatColor.RED + "Failed to find member!");
            return;
        }
        Guild g = LDSync.jda.getGuildById(LDSync.getDiscordServerId());
        if (LDSync.getRemoveHigherRolesOnSync()) removeRoles(g, member, rolesThatHas(member, findHigherRoles(role)));
        g.addRoleToMember(member, role).queue();
        sender.sendMessage(ChatColor.GREEN + "Successfully synced!");
    }
}
