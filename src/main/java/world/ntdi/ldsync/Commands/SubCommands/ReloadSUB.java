package world.ntdi.ldsync.Commands.SubCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import world.ntdi.ldsync.Commands.SubCommand;
import world.ntdi.ldsync.LDSync;
import world.ntdi.ldsync.Utils.StringUtils;

public class ReloadSUB extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the config.yml and apply changes to it";
    }

    @Override
    public String getSyntax() {
        return "/ldsync reload";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage(StringUtils.formatMessage("%logo% Reloading config.yml"));
        LDSync.getInstance().rConfig();
        sender.sendMessage(StringUtils.formatMessage("%logo% Config.yml reloaded"));
    }
}
