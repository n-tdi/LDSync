package world.ntdi.ldsync.utils;

import org.bukkit.ChatColor;
import world.ntdi.ldsync.LDSync;

public class StringUtils {
    public static String formatMessage(String str) {
        return ChatColor.translateAlternateColorCodes('&', str.replace("%logo%", LDSync.getLogo()));
    }
}
