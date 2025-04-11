package net.laboulangerie.landschat.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.kyori.adventure.text.Component;
import github.scarsz.discordsrv.hooks.chat.ChatHook;
import github.scarsz.discordsrv.util.LangUtil;
import github.scarsz.discordsrv.util.MessageUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.laboulangerie.landschat.LandsChat;
import net.laboulangerie.landschat.events.AsyncChatHookEvent;

public class DiscordHook implements ChatHook {
    // From Minecraft to Discord
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessage(AsyncChatHookEvent event) {
        String messageString = MessageUtil
                .stripMiniTokens(PlainTextComponentSerializer.plainText().serialize(event.getMessage()));

        // make sure message isn't blank
        if (StringUtils.isBlank(messageString)) {
            DiscordSRV.debug("Received blank TownyChatRemake message, not processing");
            return;
        }

        DiscordSRV.getPlugin().processChatMessage(event.getPlayer(), messageString, "global", event.isCancelled(),
                event);
    }

    // From Discord to Minecraft
    @Override
    public void broadcastMessageToChannel(String channelId, Component message) {

        String messageString = MessageUtil.toLegacy(message);

        String plainMessage = LangUtil.Message.CHAT_CHANNEL_MESSAGE.toString()
                .replace("%channelcolor%", "")
                .replace("%message%", messageString);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(plainMessage);
        }
    }

    @Override
    public Plugin getPlugin() {
        return LandsChat.PLUGIN;
    }
}
