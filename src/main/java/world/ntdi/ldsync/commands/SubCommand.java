package world.ntdi.ldsync.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import world.ntdi.ldsync.LDSync;
import world.ntdi.ldsync.utils.StringUtils;

public abstract class SubCommand {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract double getCooldown();
    public abstract void perform(CommandSender sender, String args[]);
    protected void incorrectSyntax(CommandSender sender) {
        sender.sendMessage(StringUtils.formatMessage("%logo% Invalid syntax. Use " + getSyntax()));
        if (sender instanceof Player p) {
            CommandManager.removeFromCooldown(p.getUniqueId());
        }
    }
}
