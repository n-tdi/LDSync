package world.ntdi.ldsync.bot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import world.ntdi.ldsync.LDSync;
import world.ntdi.ldsync.bot.listeners.MessageListener;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    public static void main(String[] args) throws LoginException, InterruptedException {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setServerInvite("https://discord.gg/56rdkbSqa8");
        builder.setOwnerId("811580599068262421");
        builder.setCoOwnerIds(348587937144897537L);
        builder.setPrefix("!");
        builder.useHelpBuilder(false);

        builder.setActivity(Activity.playing(LDSync.getStatus()));
        CommandClient commandClient = builder.build();

        JDA jda = JDABuilder.createDefault(LDSync.getBotToken())
                .setBulkDeleteSplittingEnabled(false)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(
                        commandClient,
                        new MessageListener())
                .build();

        jda.awaitReady();
        CommandListUpdateAction commandListUpdateAction = jda.updateCommands();
        commandListUpdateAction.queue();
        LDSync.jda = jda;
    }
}
