package p1xel.pvpmode.Storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import p1xel.pvpmode.PVPMode;

import java.io.File;
import java.io.IOException;

public class World {

    public static void createFile() {
        File file = new File(PVPMode.getInstance().getDataFolder(), "worlds.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                PVPMode.getInstance().getLogger().info("Can't create worlds file. Please ask the author for help!");
            }
        }
    }

    public static FileConfiguration get() {
        File file = new File(PVPMode.getInstance().getDataFolder(), "worlds.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void set(String path, Object value) {
        File file = new File(PVPMode.getInstance().getDataFolder(), "worlds.yml");
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set(path,value);
        try {
            yaml.save(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static String getMode(String world) {
        return get().getString(world + ".mode");
    }

    public static String getPermission(String world) {
        return get().getString(world + ".permission");
    }

    public static boolean isSet(String world) {
        return get().isSet(world + ".mode");
    }

    public static boolean isWorldModeLocked(String world) { return get().getBoolean(world + ".lock"); }

    public static void createWorld(String world, String mode) {
        set(world + ".mode", mode);
        set(world + ".lock", true);
        set(world + ".permission", Config.getString("worlds-settings.default.permission"));
    }

    public static void removeWorld(String world) {
        set(world, null);
    }

    public static void setMode(String world, String mode) {
        set(world + ".mode", mode);
    }
}
