package world.ntdi.ldsync.Commands.SubCommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import world.ntdi.ldsync.Commands.SubCommand;

public class HelpSUB extends SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "opens the help readme on github";
    }

    @Override
    public String getSyntax() {
        return "/help";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        TextComponent link = new TextComponent(ChatColor.RED + "Click " + ChatColor.BOLD + "HERE " + ChatColor.RED + "to open the help readme on github");
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/professional-tdi/LDSync/blob/main/README.md"));
        link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(ChatColor.GREEN + "Click to open the help readme on github")}));

        sender.sendMessage("");
        sender.spigot().sendMessage(link);
        sender.sendMessage("");
    }
}
