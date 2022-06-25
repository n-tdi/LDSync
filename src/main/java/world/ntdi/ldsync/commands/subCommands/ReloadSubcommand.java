package world.ntdi.ldsync.commands.subCommands;

import org.bukkit.command.CommandSender;
import world.ntdi.ldsync.commands.SubCommand;
import world.ntdi.ldsync.LDSync;
import world.ntdi.ldsync.utils.StringUtils;

public class ReloadSubcommand extends SubCommand {

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
