package world.ntdi.ldsync.commands.subCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import world.ntdi.ldsync.commands.SubCommand;
import world.ntdi.ldsync.LDSync;
import world.ntdi.ldsync.utils.LDUtils;
import world.ntdi.ldsync.utils.StringUtils;

public class SyncSubcommand extends SubCommand {

    @Override
    public String getName() {
        return "sync";
    }

    @Override
    public String getDescription() {
        return "Sync a player to their discord";
    }

    @Override
    public String getSyntax() {
        return "/ldsync sync <player> <discord name>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 2) {
            String player = args[0];
            String discord = args[1];
            if (Bukkit.getPlayer(player) == null) {
                sender.sendMessage(StringUtils.formatMessage("%logo% Player not found"));
                return;
            }
            if (!LDUtils.hasRank(Bukkit.getPlayer(player))) {
                sender.sendMessage(StringUtils.formatMessage("%logo% Player does not have a rank to sync with"));
                return;
            }
            sender.sendMessage(StringUtils.formatMessage("%logo% Syncing " + player + " to " + discord));
            LDUtils.Sync(sender, Bukkit.getPlayer(player), discord, LDSync.permissions.getPrimaryGroup(Bukkit.getPlayer(player)));
        } else {
            sender.sendMessage(StringUtils.formatMessage("%logo% Invalid syntax. Use /ldsync sync <player> <discord name>"));
        }
    }
}
