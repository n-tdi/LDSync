package world.ntdi.ldsync.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import world.ntdi.ldsync.LDSync;

public class ChatListener implements Listener {
    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        if (LDSync.getCustomChatFormat()) {
            Player p = e.getPlayer();
            String unformatted = LDSync.getCustomChatFormatString();
            String finalForm = unformatted
                    .replace("%rank%", LDSync.chat.getPlayerPrefix(p))
                    .replace("%player_name%", "%s")
                    .replace("%message%", "%s");
            e.setFormat(ChatColor.translateAlternateColorCodes('&', finalForm));
        }
    }
}
