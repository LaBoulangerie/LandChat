package net.laboulangerie.landschat.expansion;

import org.bukkit.entity.Player;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.nation.Nation;
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
            Land land = landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null);

            if(land != null){
                return "<white>[<aqua>" + land.getName().toUpperCase().replaceAll("[AÂÄEÊËIÎÏOÔÖUÛÜY]", "").substring(0, 4) + "<white>] ";
            } else {
                return "";
            }
        } else if(params.equals("nationtag")) {
            try{
                Nation nation = landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null).getNation();
                return "<white>[<gold>" + nation.getName().toUpperCase().replaceAll("[AÂÄEÊËIÎÏOÔÖUÛÜY]", "").substring(0, 4) + "<white>] ";
            } catch (Exception e) {
                return "";
            }
        } else if(params.equals("nationmembers")) return ((Integer)landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null).getNation().getMembersAmount()).toString();

        return null;
    }
}
