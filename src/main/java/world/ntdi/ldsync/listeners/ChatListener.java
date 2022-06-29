package world.ntdi.ldsync.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import world.ntdi.ldsync.LDSync;
import world.ntdi.ldsync.utils.LDUtils;

public class ChatListener implements Listener {
    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (LDSync.getCustomChatFormat()) {
            String unformatted = LDSync.getCustomChatFormatString();
            String finalForm = unformatted
                    .replace("%rank%", LDSync.chat.getPlayerPrefix(p))
                    .replace("%player_name%", "%s")
                    .replace("%msg%", "%s");
            e.setFormat(ChatColor.translateAlternateColorCodes('&', finalForm));
        }
        if (LDSync.getMinecraftChatToDiscord()) {
            LDUtils.sendToDiscord(p, e.getMessage());
        }
    }
}
