package world.ntdi.ldsync.commands.basecommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.CharUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import world.ntdi.ldsync.LDSync;
import world.ntdi.ldsync.utils.StringUtils;

import java.util.UUID;

public class SyncCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;

        String id = String.valueOf(UUID.randomUUID());

        TextComponent syncCmd = new TextComponent(ChatColor.GOLD + LDSync.getBotPrefix() + "sync " + id);
        syncCmd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, LDSync.getBotPrefix() + "sync " + id));
        syncCmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Click to copy")}));

        p.sendMessage("");
        p.sendMessage(StringUtils.formatMessage("%logo% &fClick the below command to copy it and send it in discord."));
        p.spigot().sendMessage(syncCmd);
        p.sendMessage("");

        LDSync.syncingList.put(id, p.getUniqueId());

        return true;
    }
}
