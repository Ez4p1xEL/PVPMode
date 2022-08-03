package p1xel.pvpmode.Storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import p1xel.pvpmode.PVPMode;

import java.io.File;
import java.io.IOException;

public class Data {

    public static void createFile() {
        File file = new File(PVPMode.getInstance().getDataFolder(), "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                PVPMode.getInstance().getLogger().info("Can't create data file. Please ask the author for help!");
            }
        }
    }

    public static FileConfiguration get() {
        File file = new File(PVPMode.getInstance().getDataFolder(), "data.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void set(String path, Object value) {
        File file = new File(PVPMode.getInstance().getDataFolder(), "data.yml");
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set(path,value);
        try {
            yaml.save(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static boolean isExist(String name) {
        return get().isSet(name);
    }

    public static void createPlayer(String name) {
        set(name, "peace");
    }

    public static void setMode(String name, String mode) {
        set(name, mode);
    }

    public static String getMode(String name) {
        return get().getString(name);
    }

    public static String getModeLabel(String name) {
        return Locale.getMessage(getMode(name));
    }

}
