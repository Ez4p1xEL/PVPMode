package p1xel.pvpmode.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import p1xel.pvpmode.Listeners.GUIManager;
import p1xel.pvpmode.Storage.Data;
import p1xel.pvpmode.Storage.Locale;

public final class PVPModeChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player p;
    private final String m;

    public PVPModeChangeEvent(Player player, String mode) {
        p = player;
        m = mode;

        String playerName = player.getName();
        String current = Data.getMode(playerName);
        GUIManager.instance.updateMode(player, current, mode);
        Data.setMode(playerName, mode);

        p.sendMessage(Locale.getMessage("mode-change").replaceAll("%mode%", Data.getModeLabel(p.getName())));
    }

    public Player getPlayer() {
        return p;
    }

    public String getMode() {
        return m;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
