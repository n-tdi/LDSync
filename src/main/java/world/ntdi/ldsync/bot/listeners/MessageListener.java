package world.ntdi.ldsync.bot.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import world.ntdi.ldsync.LDSync;
import world.ntdi.ldsync.utils.LDUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String msg = message.getContentDisplay();

        if (!msg.startsWith(LDSync.getBotPrefix())) return;

        if (author.isBot()) return;


        if (event.isFromType(ChannelType.TEXT)) {
            Guild guild = event.getGuild();
            if (!guild.getId().equalsIgnoreCase(LDSync.getDiscordServerId())) return;

            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();
            String command = msg.replace(LDSync.getBotPrefix(), "");
            String[] unFilteredArgs = command.split(" ");
            ArrayList<String> newArgs = new ArrayList<>(Arrays.asList(unFilteredArgs));
            newArgs.remove(0);
            String[] args = newArgs.toArray(new String[0]);

            if (command.toLowerCase(Locale.ROOT).startsWith("sync")) {
                if (args.length < 1) {
                    message.reply("This command requires at least one argument.").queue();
                    return;
                }

                String id = args[0];
                if (!LDSync.isValidId(id)) {
                    message.reply("Invalid ID.").queue();
                    return;
                }

                LDUtils.syncFromDiscord(message, member, LDSync.syncingList.get(id), guild);
                LDSync.removeFromList(id);
                message.delete().queueAfter(1, TimeUnit.SECONDS);
            }
        }
    }
}
