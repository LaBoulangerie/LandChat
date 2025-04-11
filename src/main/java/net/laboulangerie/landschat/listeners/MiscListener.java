package net.laboulangerie.landschat.listeners;

import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.laboulangerie.landschat.LandsChat;
import net.laboulangerie.landschat.core.ComponentRenderer;

public class MiscListener implements Listener {

    private ConfigurationSection miscSection;
    private ComponentRenderer componentRenderer;

    public MiscListener() {
        this.miscSection = LandsChat.PLUGIN.getConfig().getConfigurationSection("misc");
        this.componentRenderer = LandsChat.PLUGIN.getComponentRenderer();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);

        // Wait for Towny to create new resident
        new BukkitRunnable() {

            @Override
            public void run() {
                Player player = event.getPlayer();
                String joinString = player.hasPlayedBefore()
                        ? miscSection.getString("join_message")
                        : miscSection.getString("first_join_message");
                Component joinComponent = componentRenderer.parse(player, joinString,
                        Placeholder.component("username", player.name()));
                LandsChat.PLUGIN.getServer().broadcast(joinComponent);
            }
        }.runTaskLater(LandsChat.PLUGIN, 5);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String joinString = miscSection.getString("quit_message");
        Component joinComponent = componentRenderer.parse(event.getPlayer(), joinString,
                Placeholder.component("username", event.getPlayer().name()));
        event.quitMessage(joinComponent);
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        Advancement advancement = event.getAdvancement();
        AdvancementDisplay advancementDisplay = advancement.getDisplay();

        if (advancementDisplay == null || !advancementDisplay.doesAnnounceToChat())
            return;

        AdvancementDisplay.Frame frame = advancementDisplay.frame();

        String advancementString = miscSection.getString("advancement." + frame.name().toLowerCase());
        Component advancementComponent = componentRenderer.parse(event.getPlayer(), advancementString,
                TagResolver.resolver(
                        Placeholder.component("username", event.getPlayer().name()),
                        Placeholder.component("advancement_title", advancementDisplay.title()),
                        Placeholder.component("advancement_description", advancementDisplay.description())));

        event.message(advancementComponent);
    }

}
