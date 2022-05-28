package p1xel.pvpmode.Storage;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import p1xel.pvpmode.PVPMode;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Locale {

    public static void createFile() {
        List<String> lang = Arrays.asList("en","zh_CN");
        for (String l : lang) {
            File file = new File(PVPMode.getInstance().getDataFolder(), l + ".yml");
            if (!file.exists()) {
                PVPMode.getInstance().saveResource(l + ".yml", false);
            }
        }
    }

    public static FileConfiguration get() {
        File file = new File(PVPMode.getInstance().getDataFolder(), Config.getLanguage() + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', get().getString(path).replaceAll("%plugin%", get().getString("Plugin")));
    }

}
