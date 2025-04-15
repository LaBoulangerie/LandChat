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
                return "<white>[<aqua>" + tagFormat(land.getName()) + "<white>] ";
            } else {
                return "";
            }
        } else if(params.equals("nationtag")) {
            try{
                Nation nation = landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null).getNation();
                return "<white>[<gold>" + tagFormat(nation.getName()) + "<white>] ";
            } catch (Exception e){
                return "";
            }
        } else if(params.equals("nationmembers")) return ((Integer)landsAPI.getLandPlayer(player.getUniqueId()).getLands().stream().findFirst().orElse(null).getNation().getMembersAmount()).toString();

        return null;
    }

    private String tagFormat(String name){
        String nameWithVowels = name.toUpperCase();
        String nameNoVowels = name.toUpperCase().replaceAll("[AÂÄEÊËIÎÏOÔÖUÛÜY]", "");

        if(nameNoVowels.length() >= 4) return nameNoVowels.substring(0, 4);
        else if(nameWithVowels.length() >= 4) return nameWithVowels.substring(0, 4);
        else if(nameWithVowels.length() == 3) return nameWithVowels.substring(0, 3);
        else if(nameWithVowels.length() == 2) return nameWithVowels.substring(0, 2);
        else return nameWithVowels.substring(0, 1);
    }
}