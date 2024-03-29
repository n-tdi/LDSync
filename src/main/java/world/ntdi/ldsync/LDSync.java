package world.ntdi.ldsync;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.JDA;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import world.ntdi.ldsync.bot.BotThread;
import world.ntdi.ldsync.commands.CommandManager;
import world.ntdi.ldsync.commands.basecommands.SyncCommandExecutor;
import world.ntdi.ldsync.listeners.ChatListener;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class LDSync extends JavaPlugin {
    public static FileConfiguration config;
    public static LDSync ldSync;
    public static Thread botThread;
    public static Permission permissions;
    public static Chat chat;
    public static JDA jda;
    private static final String SPIGOT_RESOURCE_ID = ""+102933;
    public static Map<String, UUID> syncingList = new HashMap<>();
    public static Boolean placeHolderApiUsed;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ldSync = this;
        config = getConfig();
        config.addDefault("bot-token", "token-here");
        config.addDefault("discord-server-id", "discord-server-id-here");
        config.addDefault("bot-prefix", "$");
        config.addDefault("remove-higher-roles-on-sync", true);
        config.addDefault("custom-chat-format", true);
        config.addDefault("custom-chat-format-string", "%prefix% %player_name%: &f%msg%");
        config.addDefault("minecraft-chat-to-discord", false);
        config.addDefault("minecraft-chat-to-discord-channel-id", "0000000000");
        config.addDefault("discord-to-minecraft-chat-format", "&c&lDISCORD &7> &b%username%: &f%msg%");
        config.addDefault("discord-chat", true);
        config.addDefault("discord-chat-format-string", "**%prefix% %player_name%** %msg%");
        config.addDefault("logo", "&cLD&lSYNC&7");
        config.addDefault("status", "minecraft");
        config.options().copyDefaults(true);
        saveConfig();

        if(getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
            RegisteredServiceProvider<Chat> rsp1 = getServer().getServicesManager().getRegistration(Chat.class);
            if(rsp != null) {
                permissions = rsp.getProvider();
                getLogger().info("hooked into permissions");
            }
            if(rsp1 != null) {
                chat = rsp1.getProvider();
                getLogger().info("hooked into chat");
            }
        } else {
            getLogger().severe("Vault not found!");
        }

        placeHolderApiUsed = getServer().getPluginManager().getPlugin("PlaceholderApi") != null;

        BotThread thread = new BotThread();
        botThread = thread;
        thread.start();

        try {
            registerCommand("ldsync", new CommandManager(), new CommandManager());
            registerCommand("sync", new SyncCommandExecutor(), null);
        } catch (Exception e) {
            getLogger().severe("Something has gone horribly wrong registering ldsync commands");
            e.printStackTrace();
        }
        registerListener(new ChatListener());

        new UpdateChecker(this, UpdateCheckSource.SPIGOT, SPIGOT_RESOURCE_ID)
                .setDownloadLink("https://www.spigotmc.org/resources/ldsync.102933/download?version=458222") // You can either use a custom URL or the Spigot Resource ID
                .setDonationLink("https://buymeacoffee.com/ntdi")
                .setChangelogLink(SPIGOT_RESOURCE_ID) // Same as for the Download link: URL or Spigot Resource ID
                .setNotifyOpsOnJoin(true) // Notify OPs on Join when a new version is found (default)
                .checkEveryXHours(8)
                .checkNow();

        getLogger().info("LDSync has been enabled - by Ntdi");
    }

    public void registerCommand(String name, CommandExecutor executor, @Nullable TabCompleter tabCompleter) {
        getCommand(name).setExecutor(executor);
        if(tabCompleter != null) {
            getCommand(name).setTabCompleter(tabCompleter);
        }
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        try {
            jda.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getBotToken() {
        return config.getString("bot-token");
    }

    public static String getDiscordServerId() {
        return config.getString("discord-server-id");
    }
    public static String getLogo() {
        return config.getString("logo");
    }
    public static boolean getRemoveHigherRolesOnSync() {
        return config.getBoolean("remove-higher-roles-on-sync");
    }
    public static boolean getCustomChatFormat() {
        return config.getBoolean("custom-chat-format");
    }
    public static String getCustomChatFormatString() {
        return config.getString("custom-chat-format-string");
    }

    public static String getBotPrefix() {
        return config.getString("bot-prefix");
    }

    public static boolean getMinecraftChatToDiscord() {
        return config.getBoolean("minecraft-chat-to-discord");
    }

    public static String getMinecraftChatToDiscordChannelId() {
        return config.getString("minecraft-chat-to-discord-channel-id");
    }

    public static String getDiscordToMinecraftChatFormat() {
        return config.getString("discord-to-minecraft-chat-format");
    }
    
    public static String getStatus() {
        return config.getString("status");
    }

    public static Boolean getDiscordChat() {
        return config.getBoolean("discord-chat");
    }

    public static String getDiscordChatFormatString() {
        return config.getString("discord-chat-format-string");
    }

    public void restartThread() {
        botThread.interrupt();
        botThread = new BotThread();
        botThread.start();
    }

    public void rConfig() {
        reloadConfig();
        config = ldSync.getConfig();
        restartThread();
    }

    public static boolean isValidId(String id) {
        return syncingList.containsKey(id);
    }

    public static void removeFromList(String id) {
        syncingList.remove(id);
    }

    public static LDSync getInstance() {
        return ldSync;
    }
}
