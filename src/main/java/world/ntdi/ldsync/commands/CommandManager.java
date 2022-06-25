package world.ntdi.ldsync.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import world.ntdi.ldsync.commands.subcommands.HelpSubcommand;
import world.ntdi.ldsync.commands.subcommands.ReloadSubcommand;
import world.ntdi.ldsync.commands.subcommands.SyncSubcommand;
import world.ntdi.ldsync.LDSync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final ArrayList<SubCommand> SUBCOMMANDS = new ArrayList<>(Arrays.asList(new ReloadSubcommand(), new SyncSubcommand(), new HelpSubcommand()));

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
                SubCommand sub = getSubcommands().get(i);
                String synt = ChatColor.DARK_PURPLE + sub.getSyntax();
                String recolored = synt.replaceFirst("/", ChatColor.DARK_GRAY + "/" + ChatColor.BLUE);
                String desc = ChatColor.LIGHT_PURPLE + sub.getDescription();
                String sep = ChatColor.DARK_GRAY + " - ";

                sender.sendMessage(recolored + sep + desc);
            }
            sender.sendMessage(); sender.sendMessage(ChatColor.DARK_GRAY + "---------------------------------------------");
        } return true;
    }

    public ArrayList<SubCommand> getSubcommands(){ return SUBCOMMANDS; }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(command.getName().toLowerCase(Locale.ROOT).startsWith("lds") && args.length <= 1){
            List<String> list = new ArrayList<>();
            for (SubCommand sub : getSubcommands()) {
                list.add(sub.getName());
            }
            return list;
        }
        return null;
    }
}
