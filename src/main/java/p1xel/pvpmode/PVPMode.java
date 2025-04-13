package p1xel.pvpmode;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import p1xel.pvpmode.Command.Cmd;
import p1xel.pvpmode.Command.TabList;
import p1xel.pvpmode.Hook.HPAPI;
import p1xel.pvpmode.Listeners.GUIManager;
import p1xel.pvpmode.Listeners.ModeListener;
import p1xel.pvpmode.Listeners.ModeLock;
import p1xel.pvpmode.SpigotMC.UpdateChecker;
import p1xel.pvpmode.Storage.Config;
import p1xel.pvpmode.Storage.Data;
import p1xel.pvpmode.Storage.Locale;
import p1xel.pvpmode.Storage.World;
import p1xel.pvpmode.bStats.Metrics;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class PVPMode extends JavaPlugin {

    private static PVPMode instance;

    public static PVPMode getInstance() {
        return instance;
    }

    private static Economy econ = null;

    public static Logger log = Logger.getLogger("Minecraft");

    public static boolean isVaultEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("Vault");
    }

    public static List<String> getModeList() {
        return Arrays.asList("peace","default","insane");
    }

    @Deprecated
    public static boolean isCitizenEnabled() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Citizens") == null) {
            return false;
        }
        if (!Config.getBool("hook.citizens")) {
            return false;
        }
        return true;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Locale.createFile();
        Data.createFile();
        World.createFile();

        getServer().getPluginCommand("PVPMode").setExecutor(new Cmd());
        getServer().getPluginCommand("PVPMode").setTabCompleter(new TabList());
        getServer().getPluginManager().registerEvents(new ModeLock(), this);
        getServer().getPluginManager().registerEvents(new ModeListener(), this);
        getServer().getPluginManager().registerEvents(new GUIManager(), this);

        // Vault
        if (setupEconomy() ) {
            log.info("Vault function loaded!");
        } else {
            log.info("Can't find Vault. Disabling function ...");
        }
        //

        // PlaceholderAPI
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HPAPI().register();
            log.info("PlaceholderAPI function loaded!");
        } else {
            log.info("Can't find PlaceholderAPI. Disabling function ...");
        }
        //

        // bStats
        int pluginId = 15324;
        new Metrics(this, pluginId);
        //

        getLogger().info("PVPMode " + Config.getVersion() + " loaded!");

        if (Config.getBool("check-update")) {
            new UpdateChecker(this, 102262).getVersion(version -> {
                if (this.getDescription().getVersion().equals(version)) {
                    getLogger().info(Locale.getMessage("update-check.latest"));
                } else {
                    getLogger().info(Locale.getMessage("update-check.outdate"));
                }
            });
        }

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

}
