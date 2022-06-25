package world.ntdi.ldsync.Utils;

import org.bukkit.ChatColor;
import world.ntdi.ldsync.LDSync;

public class StringUtils {
    public static String formatMessage(String str) {
        return ChatColor.translateAlternateColorCodes('&', str.replace("%logo%", LDSync.getLogo()));
    }
}
