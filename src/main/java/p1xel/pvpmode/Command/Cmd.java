package p1xel.pvpmode.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import p1xel.pvpmode.Event.PVPModeChangeEvent;
import p1xel.pvpmode.Listeners.GUIManager;
import p1xel.pvpmode.PVPMode;
import p1xel.pvpmode.Storage.Config;
import p1xel.pvpmode.Storage.Data;
import p1xel.pvpmode.Storage.Locale;
import p1xel.pvpmode.Storage.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class Cmd implements CommandExecutor {

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Locale.getMessage("commands.help"));
                return true;
            }

            Player p = (Player) sender;
            p.openInventory(GUIManager.getPlayerMenu(p));
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {

                sender.sendMessage(Locale.getMessage("commands.top"));
                sender.sendMessage(Locale.getMessage("commands.top-space"));
                sender.sendMessage(Locale.getMessage("commands.plugin").replaceAll("%version%", Config.getVersion()));
                sender.sendMessage(Locale.getMessage("commands.empty"));
                sender.sendMessage(Locale.getMessage("commands.help"));
                sender.sendMessage(Locale.getMessage("commands.info"));
                sender.sendMessage(Locale.getMessage("commands.mode"));
                if (sender.hasPermission("pvpmode.admin")) {
                    sender.sendMessage(Locale.getMessage("commands.mode-other"));
                    sender.sendMessage(Locale.getMessage("commands.create"));
                    sender.sendMessage(Locale.getMessage("commands.remove"));
                    sender.sendMessage(Locale.getMessage("commands.world-mode"));
                    sender.sendMessage(Locale.getMessage("commands.reload"));
                }
                sender.sendMessage(Locale.getMessage("commands.bottom-space"));
                sender.sendMessage(Locale.getMessage("commands.bottom"));
                return true;

            }

            if (args[0].equalsIgnoreCase("info")) {

                sender.sendMessage(Locale.getMessage("mode-info").replaceAll("%player%", sender.getName()).replaceAll("%mode%", Data.getModeLabel(sender.getName())));
                return true;

            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("pvpmode.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                }

                Config.reloadConfig();
                Data.createFile();
                Locale.createFile();
                World.createFile();
                GUIManager.instance.clearPlayers();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    GUIManager.instance.createInventory(player);
                }
                sender.sendMessage(Locale.getMessage("reload-success"));
                return true;

            }

            if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("peace")) {

                if (!(sender instanceof Player)) {
                    sender.sendMessage(Locale.getMessage("must-be-player"));
                    return true;
                }

                Player p = (Player) sender;

                if (Config.getStringList("blacklist-world").contains(p.getWorld().getName())) {
                    sender.sendMessage(Locale.getMessage("blacklist-world"));
                    return true;
                }

                if (World.isWorldModeLocked(p.getWorld().getName())) {
                    sender.sendMessage(Locale.getMessage("mode-lock"));
                    return true;
                }

                PVPModeChangeEvent event = new PVPModeChangeEvent(p, "peace");
                Bukkit.getServer().getPluginManager().callEvent(event);
                sender.sendMessage(Locale.getMessage("mode-success").replaceAll("%mode%", Data.getModeLabel(sender.getName())));
                return true;
            }

            if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("default")) {

                if (!(sender instanceof Player)) {
                    sender.sendMessage(Locale.getMessage("must-be-player"));
                    return true;
                }

                Player p = (Player) sender;

                if (Config.getStringList("blacklist-world").contains(p.getWorld().getName())) {
                    sender.sendMessage(Locale.getMessage("blacklist-world"));
                    return true;
                }

                if (World.isWorldModeLocked(p.getWorld().getName())) {
                    sender.sendMessage(Locale.getMessage("mode-lock"));
                    return true;
                }

                PVPModeChangeEvent event = new PVPModeChangeEvent(p, "default");
                Bukkit.getServer().getPluginManager().callEvent(event);
                sender.sendMessage(Locale.getMessage("mode-success").replaceAll("%mode%", Data.getModeLabel(sender.getName())));
                return true;
            }

            if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("insane")) {

                if (!(sender instanceof Player)) {
                    sender.sendMessage(Locale.getMessage("must-be-player"));
                    return true;
                }

                if (!sender.hasPermission("pvpmode.insane")) {
                    sender.sendMessage(Locale.getMessage("insane-no-perm"));
                    return true;
                }

                Player p = (Player) sender;

                if (Config.getBool("insane.timer.enable")) {
                    if (Data.getMode(sender.getName()).equalsIgnoreCase("insane")) {
                        p.sendMessage(Locale.getMessage("already-is").replaceAll("%mode%", Data.getMode(sender.getName())));
                        return true;
                    }
                }

                if (PVPMode.isVaultEnabled()) {
                    if (PVPMode.getEconomy().getBalance(p) < Config.getInsaneCost()) {
                        sender.sendMessage(Locale.getMessage("no-money").replaceAll("%money%", String.valueOf(Config.getInsaneCost())));
                        return true;
                    } else {
                        PVPMode.getEconomy().withdrawPlayer(p, Config.getInsaneCost());
                    }
                } else {
                    sender.sendMessage(Locale.getMessage("vault-disable"));
                    return true;
                }

                if (Config.getStringList("blacklist-world").contains(p.getWorld().getName())) {
                    sender.sendMessage(Locale.getMessage("blacklist-world"));
                    return true;
                }

                if (World.isWorldModeLocked(p.getWorld().getName())) {
                    sender.sendMessage(Locale.getMessage("mode-lock"));
                    return true;
                }

                if (Config.getBool("insane.timer.enable")) {
                    sender.sendMessage(Locale.getMessage("timer-start").replaceAll("%time%", String.valueOf(Config.getInt("insane.timer.time"))));
                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            if (!World.isWorldModeLocked(p.getWorld().getName())) {
                                PVPModeChangeEvent event = new PVPModeChangeEvent(p, "default");
                                Bukkit.getServer().getPluginManager().callEvent(event);
                                sender.sendMessage(Locale.getMessage("timer-end"));
                            } else {
                                PVPModeChangeEvent event = new PVPModeChangeEvent(p, World.getMode(p.getWorld().getName()));
                                Bukkit.getServer().getPluginManager().callEvent(event);
                                sender.sendMessage(Locale.getMessage("timer-end"));
                            }

                        }

                    }.runTaskLater(PVPMode.getInstance(), Config.getInt("insane.timer.time") * 20L);
                }

                PVPModeChangeEvent event = new PVPModeChangeEvent(p, "insane");
                Bukkit.getServer().getPluginManager().callEvent(event);
                sender.sendMessage(Locale.getMessage("mode-success").replaceAll("%mode%", Data.getModeLabel(sender.getName())));
                return true;
            }

        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("info")) {

                if (!Data.isExist(args[1])) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                sender.sendMessage(Locale.getMessage("mode-info").replaceAll("%player%", args[1]).replaceAll("%mode%", Data.getModeLabel(args[1])));
                return true;

            }

            if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("peace")) {

                if (!sender.hasPermission("pvpmode.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                Player p = (Player) sender;
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                if (Config.getStringList("blacklist-world").contains(target.getWorld().getName())) {
                    sender.sendMessage(Locale.getMessage("blacklist-world"));
                    return true;
                }
                PVPModeChangeEvent event = new PVPModeChangeEvent(target, "peace");
                Bukkit.getServer().getPluginManager().callEvent(event);
                sender.sendMessage(Locale.getMessage("mode-other-success").replaceAll("%player%", args[1]).replaceAll("%mode%", Data.getModeLabel(args[1])));
                return true;
            }

            if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("default")) {

                if (!sender.hasPermission("pvpmode.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                Player p = (Player) sender;
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                if (Config.getStringList("blacklist-world").contains(target.getWorld().getName())) {
                    sender.sendMessage(Locale.getMessage("blacklist-world"));
                    return true;
                }

                PVPModeChangeEvent event = new PVPModeChangeEvent(target, "default");
                Bukkit.getServer().getPluginManager().callEvent(event);
                sender.sendMessage(Locale.getMessage("mode-other-success").replaceAll("%player%", args[1]).replaceAll("%mode%", Data.getModeLabel(args[1])));
                return true;
            }

            if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("insane")) {

                if (!sender.hasPermission("pvpmode.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                if (!sender.hasPermission("pvpmode.insane")) {
                    sender.sendMessage(Locale.getMessage("insane-no-perm"));
                    return true;
                }

                Player p = (Player) sender;
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                if (Config.getStringList("blacklist-world").contains(target.getWorld().getName())) {
                    sender.sendMessage(Locale.getMessage("blacklist-world"));
                    return true;
                }

                PVPModeChangeEvent event = new PVPModeChangeEvent(target, "insane");
                Bukkit.getServer().getPluginManager().callEvent(event);
                sender.sendMessage(Locale.getMessage("mode-other-success").replaceAll("%player%", args[1]).replaceAll("%mode%", Data.getModeLabel(args[1])));
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {

                if (!sender.hasPermission("pvpmode.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                String world = args[1];

                Player p = (Player) sender;

                if (!World.isSet(world)) {
                    sender.sendMessage(Locale.getMessage("invalid-world"));
                    return true;
                }

                World.removeWorld(world);
                sender.sendMessage(Locale.getMessage("remove-success").replaceAll("%world%", world));

                if (Config.getBool("insane.timer.enable")) {
                    if (Data.getMode(sender.getName()).equalsIgnoreCase("insane")) {
                        PVPModeChangeEvent event = new PVPModeChangeEvent(p, Config.getString("default-mode"));
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    }
                }

                return true;

            }


        }

        if (args.length == 3) {

            if (args[0].equalsIgnoreCase("create")) {

                if (!sender.hasPermission("pvpmode.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                String world = args[1];
                String mode = args[2];

                if (World.isSet(world)) {
                    sender.sendMessage(Locale.getMessage("already-exist"));
                    return true;
                }

                if (!PVPMode.getModeList().contains(mode)) {
                    sender.sendMessage(Locale.getMessage("wrong-mode"));
                    return true;
                }

                World.createWorld(world, mode);
                sender.sendMessage(Locale.getMessage("create-success").replaceAll("%world%", world));

                for (Player allp : Bukkit.getOnlinePlayers()) {
                    if (allp.getWorld().getName().equalsIgnoreCase(world)) {
                        PVPModeChangeEvent event = new PVPModeChangeEvent(allp, World.getMode(world));
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    }
                }

                return true;

            }

            if (args[0].equalsIgnoreCase("mode")) {

                if (!sender.hasPermission("pvpmode.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                String world = args[1];
                String mode = args[2];

                if (!World.isSet(world)) {
                    sender.sendMessage(Locale.getMessage("invalid-world"));
                    return true;
                }

                if (!PVPMode.getModeList().contains(mode)) {
                    sender.sendMessage(Locale.getMessage("wrong-mode"));
                    return true;
                }

                World.setMode(world, mode);
                sender.sendMessage(Locale.getMessage("world-mode-success").replaceAll("%world%", world).replaceAll("%mode%", mode));

                for (Player allp : Bukkit.getOnlinePlayers()) {
                    if (allp.getWorld().getName().equalsIgnoreCase(world)) {
                        PVPModeChangeEvent event = new PVPModeChangeEvent(allp, World.getMode(world));
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    }
                }

                return true;

            }


        }










        return false;

    }

}
