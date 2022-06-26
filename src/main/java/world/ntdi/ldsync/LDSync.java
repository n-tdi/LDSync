package world.ntdi.ldsync;

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
import world.ntdi.ldsync.listeners.ChatListener;

import javax.annotation.Nullable;

public final class LDSync extends JavaPlugin {
    public static FileConfiguration config;
    public static LDSync ldSync;
    public static Thread botThread;
    public static Permission permissions;
    public static Chat chat;
    public static JDA jda;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ldSync = this;
        config = getConfig();
        config.addDefault("bot-token", "token-here");
        config.addDefault("discord-server-id", "discord-server-id-here");
        config.addDefault("remove-higher-roles-on-sync", true);
        config.addDefault("custom-chat-format", true);
        config.addDefault("custom-chat-format-string", "%rank% %player_name%: &f%msg%");
        config.addDefault("logo", "&cLD&lSYNC&7");
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

        BotThread thread = new BotThread();
        botThread = thread;
        thread.start();

        try {
            registerCommand("ldsync", new CommandManager(), new CommandManager());
        } catch (Exception e) {
            getLogger().severe("Something has gone horribly wrong registering ldsync commands");
            e.printStackTrace();
        }
        registerListener(new ChatListener());
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
        // Plugin shutdown logic
    }

    public static String getBotToken() {
        return ldSync.config.getString("bot-token");
    }

    public static String getDiscordServerId() {
        return ldSync.config.getString("discord-server-id");
    }
    public static String getLogo() {
        return ldSync.config.getString("logo");
    }
    public static boolean getRemoveHigherRolesOnSync() {
        return ldSync.config.getBoolean("remove-higher-roles-on-sync");
    }
    public static boolean getCustomChatFormat() {
        return ldSync.config.getBoolean("custom-chat-format");
    }
    public static String getCustomChatFormatString() {
        return ldSync.config.getString("custom-chat-format-string");
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

    public static LDSync getInstance() {
        return ldSync;
    }
}
