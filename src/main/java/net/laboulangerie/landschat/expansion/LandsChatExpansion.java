package net.laboulangerie.landschat.expansion;

import org.bukkit.entity.Player;

import me.angeschossen.lands.api.LandsIntegration;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import net.laboulangerie.landschat.LandsChat;

public class LandsChatExpansion extends PlaceholderExpansion {
    private LandsIntegration landsAPI;

    public LandsChatExpansion(){ 
        landsAPI = LandsIntegration.of(LandsChat.PLUGIN);
    }

    @Override
    public String getAuthor() {
        return "La Boulangerie";
    }

    @Override
    public String getIdentifier() {
        return "landschat";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(params.equals("landtag")) {
            if(landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null) == null) return "";
            else return "<white>[<aqua>" + landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null).getName().substring(0, 4).toUpperCase() + "<white>] ";
        } else if(params.equals("nationtag")) {
            if(landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null).getNation() == null) return "";
            else return "<white>[<gold>" + landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null).getNation().getName().substring(0, 4).toUpperCase() + "<white>] ";
        } else if(params.equals("nationmembers")) return ((Integer)landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null).getNation().getMembersAmount()).toString();

        return null;
    }
}
