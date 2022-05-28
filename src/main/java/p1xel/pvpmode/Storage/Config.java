package p1xel.pvpmode.Storage;

import p1xel.pvpmode.PVPMode;

import java.util.List;

public class Config {

    public static String getString(String path) {
        return PVPMode.getInstance().getConfig().getString(path);
    }

    public static double getDouble(String path) {
        return PVPMode.getInstance().getConfig().getDouble(path);
    }

    public static int getInt(String path) {
        return PVPMode.getInstance().getConfig().getInt(path);
    }

    public static boolean getBool(String path) {
        return PVPMode.getInstance().getConfig().getBoolean(path);
    }

    public static List<String> getStringList(String path) {
        return PVPMode.getInstance().getConfig().getStringList(path);
    }

    public static String getLanguage() {
        return getString("Language");
    }

    public static String getVersion() {
        return getString("Version");
    }

    public static void reloadConfig() {
        PVPMode.getInstance().reloadConfig();
    }

    public static int getInsaneCost() {
        return getInt("insane.cost");
    }

}
