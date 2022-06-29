# LDSync
Sync Vault ranks to discord roles



## Usage
LDSync is a super easy plugin to use, first get the jar from the releases tab or build it yourself locally.

Note: LDSync is tested for 1.17.x but may work on other verisons.

### Config.yml
```yaml
bot-token: "token-here"
discord-server-id: "discord-server-id-here"
bot-prefix: "$"
remove-higher-roles-on-sync: true
custom-chat-format: true
custom-chat-format-string: "%rank% %player_name%: &f%msg%"
minecraft-chat-to-discord: false
minecraft-chat-to-discord-channel-id: "0000000000"
discord-to-minecraft-chat-format: "&c&lDISCORD &7> &b%username%: &f%msg%"
logo: "&cLD&bSYNC &7"
```

LDSync is very customizable by even changing the name or "logo" in chat.
#### What does each value mean?
`bot-token` is the discord bots token which can be found [here](https://discord.com/developers/applications).

`discord-server-id` is the server id of your discord, a tutorial to get that can be found [here](https://support.discord.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID-).

`bot-prefix` The prefix for the bot when running a command in discord, e.g. `$sync` where the `$` is the prefix

`remove-higher-roles-on-sync` This value is `true` by default, what it means is that if you sync someone with a role lower than the one they currently have in discord, it will remove that higher role in discord and then add the lower one. This can be false if you want.

`custom-chat-format` This value is by default true and is used to format the chat to look pretty with customizable formatting. See below.

`custom-chat-format-string` The format of the custom chat. Accepts color codes. Valid formatting variables are: `%rank%` `%player_name%` `%msg%`, none of these are required but recommended

`minecraft-chat-to-discord` This value is by default false and is used to send the chat from minecraft to discord and vice versa.

`minecraft-chat-to-discord-channel-id` This is the channel id of the channel you want to send the minecraft chat to and receive from.

`discord-to-minecraft-chat-format` This is the message sent when a message is sent in the above discord channel, Supports color codes. Valid formatting variables are: `%username%` `%msg%`, none of these are required but recommended.

`logo` Obviously, the logo that appears in chat. Change it to whatever you want! 

### Permissions
The base command is `/ldsync` which requires the premission node `ldsync.use`

For regular players to sync themselves the need the permission `ldysnc.player`, read below the command they use.

### Commands
After changing the config, reload the plugin and the bot with `/ldsync reload`

Syncing players is done with `/ldsync sync <minecraft-player> <discord-username>` 

**Example:** `/ldsync sync Ntdi Ntdi#0002`

*Discord names are CaSe-SeNsItIvE*

Players by default can sync themselves with `/sync` This will give them a command to copy and run in your discord, e.g. `$sync 1234` and then they will sync by themselves.

### Support
If you still need help feel free to create an issue on this github page.

## Enjoy!
