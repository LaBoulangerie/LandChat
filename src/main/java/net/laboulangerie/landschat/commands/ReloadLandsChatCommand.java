package net.laboulangerie.landschat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.laboulangerie.landschat.LandsChat;

public class ReloadLandsChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {

        sender.sendMessage("§bReloading plugin");
        LandsChat.PLUGIN.reloadConfig();

        sender.sendMessage("§aReload complete!");
        return false;
    }

}