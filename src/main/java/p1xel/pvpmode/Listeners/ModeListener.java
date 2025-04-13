package p1xel.pvpmode.Listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import p1xel.pvpmode.Storage.Config;
import p1xel.pvpmode.Storage.Data;
import p1xel.pvpmode.Storage.Locale;

public class ModeListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {

        if (!Data.isExist(e.getPlayer().getName())) {
            Data.createPlayer(e.getPlayer().getName());
        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        Entity ep = e.getEntity();
        Entity edamager = e.getDamager();

        if (ep.hasMetadata("NPC") || edamager.hasMetadata("NPC")) {
            return;
        }

        if (ep instanceof Player && edamager instanceof Player) {

            Player p = (Player) ep;
            Player damager = (Player) edamager;

            if (Config.getStringList("blacklist-world").contains(p.getWorld().getName())) {
                return;
            }

            if (Data.getMode(p.getName()).equalsIgnoreCase("peace") || Data.getMode(damager.getName()).equalsIgnoreCase("peace")) {

                damager.sendMessage(Locale.getMessage("pvp-is-off"));
                e.setCancelled(true);
                return;

            }


            if (Config.getBool("insane.enable")) {
                if (Data.getMode(p.getName()).equalsIgnoreCase("insane")) {
                    damager.sendMessage(Locale.getMessage("insane-trigger"));
                    e.setDamage(e.getDamage() * Config.getDouble("insane.damage.multiplier"));
                }

                if (Data.getMode(damager.getName()).equalsIgnoreCase("insane")) {
                    damager.sendMessage(Locale.getMessage("insane-trigger"));
                    e.setDamage(e.getDamage() * Config.getDouble("insane.damage.multiplier"));
                }
            }
        }

        if (ep instanceof Player && edamager instanceof Arrow) {

            Entity attacker = (Entity) ((Arrow) edamager).getShooter();

            Player p = (Player) ep;

            if (attacker instanceof Player) {

                Player damager = (Player) attacker;

                if (Data.getMode(p.getName()).equalsIgnoreCase("peace") || Data.getMode(damager.getName()).equalsIgnoreCase("peace")) {

                    damager.sendMessage(Locale.getMessage("pvp-is-off"));
                    e.setCancelled(true);
                    return;

                }


                if (Config.getBool("insane.enable")) {
                    if (Data.getMode(p.getName()).equalsIgnoreCase("insane")) {
                        damager.sendMessage(Locale.getMessage("insane-trigger"));
                        e.setDamage(e.getDamage() * Config.getDouble("insane.damage.multiplier"));
                    }

                    if (Data.getMode(damager.getName()).equalsIgnoreCase("insane")) {
                        damager.sendMessage(Locale.getMessage("insane-trigger"));
                        e.setDamage(e.getDamage() * Config.getDouble("insane.damage.multiplier"));
                    }
                }

            }

        }

    }


}
