package world.ntdi.ldsync.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TabCompletion implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(command.getName().toLowerCase(Locale.ROOT).startsWith("lds") && args.length <= 1){
            List<String> list = new ArrayList<>();
            for (SubCommand sub : new CommandManager().subCommands) {
                list.add(sub.getName());
            }
            return list;
        }
        return null;
    }
}
