package world.ntdi.ldsync.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import world.ntdi.ldsync.LDSync;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public static boolean alreadyHasUpdatedRole(Member member, String rank) {
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
        if (!checks(p, name, rankName)) {
            sender.sendMessage(ChatColor.RED + "Invalid name or rank name.");
            return;
        }
        Member member = getMember(name);
        Role r = (findRoleFromName(rankName) != null) ? findRoleFromName(rankName) : createRoleFromName(rankName);

        Guild g = LDSync.jda.getGuildById(LDSync.getDiscordServerId());
        if (LDSync.getRemoveHigherRolesOnSync()) removeRoles(g, member, rolesThatHas(member, findHigherRoles(r)));
        g.addRoleToMember(member, r).queue();
        sender.sendMessage(ChatColor.GREEN + "Successfully synced!");
    }

    public static void syncFromDiscord(Message message, Member member, UUID uuid, Guild g) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        if (op.isOnline()) {
            Player p = op.getPlayer();
            if (!checks(p, member, LDSync.permissions.getPrimaryGroup(p))) {
                message.reply("Unable to sync ranks, *Checks Failed!*").queue();
                return;
            }

            Role role = (findRoleFromName(LDSync.permissions.getPrimaryGroup(p)) != null) ? findRoleFromName(LDSync.permissions.getPrimaryGroup(p)) : createRoleFromName(LDSync.permissions.getPrimaryGroup(p));
            if(role == null) {
                message.reply("Unable to sync ranks, *Failed to find role!*").queue();
                return;
            }

            if (LDSync.getRemoveHigherRolesOnSync()) removeRoles(g, member, rolesThatHas(member, findHigherRoles(role)));
            g.addRoleToMember(member, role).queue();
            message.reply("Successfully synced!").queue();
        }
    }

    public static boolean checks(Player p, String name, String rankName) {
        if(alreadyHasUpdatedRole(name, rankName)) {
            return false;
        }
        Role role = (findRoleFromName(rankName) != null) ? findRoleFromName(rankName) : createRoleFromName(rankName);
        if(role == null) {
            return false;
        }
        if(!hasRank(p)) {
            return false;
        }

        return true;
    }
    private static boolean checks(Player p, Member member, String rankName) {
        if(alreadyHasUpdatedRole(member, rankName)) {
            return false;
        }
        Role role = (findRoleFromName(rankName) != null) ? findRoleFromName(rankName) : createRoleFromName(rankName);
        if(role == null) {
            return false;
        }
        if(!hasRank(p)) {
            return false;
        }

        return true;
    }
}