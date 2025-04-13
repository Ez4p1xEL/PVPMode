package p1xel.pvpmode.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import p1xel.pvpmode.Event.PVPModeChangeEvent;
import p1xel.pvpmode.PVPMode;
import p1xel.pvpmode.Storage.World;

public class ModeLock implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {

        Player p = e.getPlayer();
        String world = e.getPlayer().getWorld().getName();
        String mode = World.getMode(world);
        String perm = World.getPermission(world);

        if (World.isSet(world)) {

            if (PVPMode.getModeList().contains(mode)) {

                if (!p.hasPermission(perm)) {

                    PVPModeChangeEvent event = new PVPModeChangeEvent(p, mode);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                }

            }

        }

    }

}
