package net.laboulangerie.landschat;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.Arrays;

import me.angeschossen.lands.api.LandsIntegration;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import github.scarsz.discordsrv.DiscordSRV;
import net.laboulangerie.landschat.core.ComponentRenderer;
import net.laboulangerie.landschat.core.LandsChatRenderer;
import net.laboulangerie.landschat.listeners.DiscordHook;
import net.laboulangerie.landschat.listeners.MiscListener;
import net.laboulangerie.landschat.listeners.LandsChatListener;

public class LandsChat extends JavaPlugin {
    public static LandsChat PLUGIN;

    public static LandsIntegration landsAPI;

    private ComponentRenderer componentRenderer;
    private LandsChatRenderer townyChatRenderer;

    private ArrayList<Listener> listeners;

    @Override
    public void onEnable() {
        LandsChat.PLUGIN = this;
        this.saveDefaultConfig();

        this.componentRenderer = new ComponentRenderer();
        this.townyChatRenderer = new LandsChatRenderer();

        this.listeners = new ArrayList<>(Arrays.asList(
                new LandsChatListener(),
                new MiscListener()));

        // Is DiscordSRV enabled? It's a softdepend
        if (getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
            DiscordHook discordHook = new DiscordHook();

            this.listeners.add(discordHook);
            this.registerListeners();
            DiscordSRV.getPlugin().getPluginHooks().add(discordHook);
            getLogger().info("Hooked to DiscordSRV!");
        } else {
            this.registerListeners();
        }

        getLogger().info("Plugin started");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled");
    }

    public ComponentRenderer getComponentRenderer() {
        return this.componentRenderer;
    }

    public LandsChatRenderer getTownyChatRenderer() {
        return this.townyChatRenderer;
    }

    private void registerListeners() {
        listeners.forEach(l -> this.getServer().getPluginManager().registerEvents(l, this));
    }

    public void registerCommand(String name, CommandExecutor executor) {
        try {
            final Constructor<PluginCommand> c;
            c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            final PluginCommand command = c.newInstance(name, this);
            command.setExecutor(executor);

            Bukkit.getCommandMap().register(this.getName(), command);
        } catch (Exception e) {
            getLogger().warning("Couldn't register command '" + name + "' cause: " + e.getMessage());
        }
    }
}