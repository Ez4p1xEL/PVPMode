package p1xel.pvpmode;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import p1xel.pvpmode.Command.Cmd;
import p1xel.pvpmode.Hook.HPAPI;
import p1xel.pvpmode.Listeners.ModeListener;
import p1xel.pvpmode.Storage.Config;
import p1xel.pvpmode.Storage.Data;
import p1xel.pvpmode.Storage.Locale;
import p1xel.pvpmode.bStats.Metrics;

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

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Locale.createFile();
        Data.createFile();

        getServer().getPluginCommand("PVPMode").setExecutor(new Cmd());
        getServer().getPluginManager().registerEvents(new ModeListener(), this);

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
