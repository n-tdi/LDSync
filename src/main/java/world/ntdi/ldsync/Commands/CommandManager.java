package world.ntdi.ldsync.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import world.ntdi.ldsync.Commands.SubCommands.ReloadSUB;
import world.ntdi.ldsync.Commands.SubCommands.SyncSUB;
import world.ntdi.ldsync.LDSync;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager implements CommandExecutor {
    public final ArrayList<SubCommand> subCommands = new ArrayList<>(Arrays.asList(new ReloadSUB(), new SyncSUB()));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0){
            for (int i = 0; i < getSubcommands().size(); i++){
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                    ArrayList<String> newArgs = new ArrayList<>(Arrays.asList(args));
                    newArgs.remove(0);
                    String[] newArgsArray = newArgs.toArray(new String[0]);
                    getSubcommands().get(i).perform(sender, newArgsArray);
                }
            }
        }else {
            sender.sendMessage(ChatColor.DARK_GRAY + "----------------" + ChatColor.translateAlternateColorCodes('&', LDSync.getLogo()) + ChatColor.DARK_GRAY +"----------------"); sender.sendMessage();
            for (int i = 0; i < getSubcommands().size(); i++){
                SubCommand sub = subCommands.get(i);
                String synt = ChatColor.DARK_PURPLE + sub.getSyntax();
                String recolored = synt.replaceFirst("/", ChatColor.DARK_GRAY + "/" + ChatColor.BLUE);
                String desc = ChatColor.LIGHT_PURPLE + sub.getDescription();
                String sep = ChatColor.DARK_GRAY + " - ";

                sender.sendMessage(recolored + sep + desc);
            }
            sender.sendMessage(); sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
        } return true;
    }

    public ArrayList<SubCommand> getSubcommands(){ return subCommands; }
}
