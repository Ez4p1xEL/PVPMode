package p1xel.pvpmode.Hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import p1xel.pvpmode.Storage.Config;
import p1xel.pvpmode.Storage.Data;

public class HPAPI extends PlaceholderExpansion {

    public String getIdentifier() {
        return "pvpmode";
    }

    public String getAuthor() {
        return "Ez4p1xEL";
    }

    public String getVersion() {
        return Config.getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {

        if(identifier.equalsIgnoreCase("mode")){
            return Data.getModeLabel(player.getName());
        }

        return null;
    }

}
