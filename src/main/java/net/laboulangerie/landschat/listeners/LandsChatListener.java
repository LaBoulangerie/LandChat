package net.laboulangerie.landschat.listeners;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.player.LandPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;

import net.laboulangerie.landschat.LandsChat;
import net.laboulangerie.landschat.core.LandsChatRenderer;
import net.laboulangerie.landschat.events.AsyncChatHookEvent;

public class LandsChatListener implements Listener {
    private LandsChatRenderer townyChatRenderer;
    private LandsIntegration landsAPI;

    public LandsChatListener() {
        this.townyChatRenderer = LandsChat.PLUGIN.getTownyChatRenderer();
        this.landsAPI = LandsChat.landsAPI;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        event.renderer(ChatRenderer.viewerUnaware(townyChatRenderer));
        event.viewers().clear();

        Player player = event.getPlayer();

        @SuppressWarnings("unused")
        LandPlayer resident = landsAPI.getLandPlayer(player.getUniqueId());

        Set<LandPlayer> residents = new HashSet<>();

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            residents.add(landsAPI.getLandPlayer(onlinePlayer.getUniqueId()));
        }

        Set<Player> recipients = residents.stream().map(LandPlayer::getPlayer)
                // Filter null players
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        event.viewers().addAll(recipients);
        event.viewers().add(Bukkit.getConsoleSender());

        AsyncChatHookEvent hookEvent = new AsyncChatHookEvent(event, !Bukkit.isPrimaryThread());
        Bukkit.getPluginManager().callEvent(hookEvent);
    }
}