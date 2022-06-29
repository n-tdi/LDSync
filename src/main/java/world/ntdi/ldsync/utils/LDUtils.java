package world.ntdi.ldsync.utils;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import world.ntdi.ldsync.LDSync;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

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

                message.getChannel().sendMessage("Unable to sync ranks, *Checks Failed!*")
                        .delay(10, SECONDS) // delete 10 seconds later
                        .flatMap(Message::delete);
                return;
            }

            Role role = (findRoleFromName(LDSync.permissions.getPrimaryGroup(p)) != null) ? findRoleFromName(LDSync.permissions.getPrimaryGroup(p)) : createRoleFromName(LDSync.permissions.getPrimaryGroup(p));
            if(role == null) {
                message.getChannel().sendMessage("Unable to sync ranks, *Failed to find role!*")
                        .delay(10, SECONDS) // delete 10 seconds later
                        .flatMap(Message::delete);
                return;
            }

            if (LDSync.getRemoveHigherRolesOnSync()) removeRoles(g, member, rolesThatHas(member, findHigherRoles(role)));
            g.addRoleToMember(member, role).queue();
            message.getChannel().sendMessage("Successfully synced!")
                    .delay(10, SECONDS) // delete 10 seconds later
                    .flatMap(Message::delete);
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

    public RestAction<Void> selfDestruct(MessageChannel channel, String content) {
        return channel.sendMessage(content)
                .delay(10, SECONDS) // delete 10 seconds later
                .flatMap(Message::delete);
    }

    public static void sendToDiscord(Player p, String message) {
        String prefix = LDSync.chat.getPlayerPrefix(p);
        TextChannel channel = LDSync.jda.getTextChannelById(LDSync.getMinecraftChatToDiscordChannelId());

        if (channel != null) {
            channel.sendMessage("**" + prefix + p.getName() + ":** " + message).queue();
        }
    }

    public static void sendToMinecraft(String author, String message) {
        String format = LDSync.getDiscordToMinecraftChatFormat()
                .replace("%username%", author)
                .replace("%msg%", message);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', format));
    }
}
