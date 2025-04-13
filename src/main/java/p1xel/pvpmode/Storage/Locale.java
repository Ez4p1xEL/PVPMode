package p1xel.pvpmode.Storage;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import p1xel.pvpmode.PVPMode;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Locale {

    static File file;
    static FileConfiguration yaml;

    public static void createFile() {
        List<String> lang = Arrays.asList("en","zh_CN");
        for (String l : lang) {
            File filew = new File(PVPMode.getInstance().getDataFolder(), l + ".yml");
            if (!filew.exists()) {
                PVPMode.getInstance().saveResource(l + ".yml", false);
            }
        }

        upload(new File(PVPMode.getInstance().getDataFolder(), Config.getLanguage() + ".yml"));
    }

    public static void upload(File filew) {
        file = filew;
        yaml = YamlConfiguration.loadConfiguration(filew);
    }

    public static FileConfiguration get() {
        return yaml;
    }

    public static String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', get().getString(path).replaceAll("%plugin%", get().getString("Plugin")));
    }

    public static String getGUIMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', get().getString(path));
    }

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message).replaceAll("%plugin%", get().getString("Plugin"));
    }

}
