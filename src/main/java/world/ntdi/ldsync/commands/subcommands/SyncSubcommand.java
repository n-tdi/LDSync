package world.ntdi.ldsync.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
            Player p = Bukkit.getPlayer(player);
            if (p == null) {
                sender.sendMessage(StringUtils.formatMessage("%logo% Player not found"));
                return;
            }
            if (!LDUtils.hasRank(p)) {
                sender.sendMessage(StringUtils.formatMessage("%logo% Player does not have a rank to sync with"));
                return;
            }
            if (!(discord.length() >= 5)) {
                sender.sendMessage(StringUtils.formatMessage("%logo% Must be valid discord name"));
                return;
            }
            if (!discord.contains("#")) {
                sender.sendMessage(StringUtils.formatMessage("%logo% Invalid discord name please format with a tag! e.g. Ntdi#0002"));
                return;
            }
            sender.sendMessage(StringUtils.formatMessage("%logo% Syncing " + player + " to " + discord));
            LDUtils.Sync(sender, p, discord, LDSync.permissions.getPrimaryGroup(p));
        } else {
            sender.sendMessage(StringUtils.formatMessage("%logo% Invalid syntax. Use /ldsync sync <player> <discord name>"));
        }
    }
}
