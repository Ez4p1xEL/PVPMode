package p1xel.pvpmode.Storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import p1xel.pvpmode.PVPMode;

import java.io.File;
import java.io.IOException;

public class Data {

    static File file;
    static FileConfiguration yaml;

    public static void createFile() {
        File filew = new File(PVPMode.getInstance().getDataFolder(), "data.yml");
        if (!filew.exists()) {
            try {
                filew.createNewFile();
            } catch (IOException ioException) {
                PVPMode.getInstance().getLogger().info("Can't create data file. Please ask the author for help!");
            }
        }
        upload(filew);
    }

    public static void upload(File filew) {
        file = filew;
        yaml = YamlConfiguration.loadConfiguration(filew);
    }

    public static FileConfiguration get() {
        return yaml;
    }

    public static void set(String path, Object value) {
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
