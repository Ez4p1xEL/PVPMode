package p1xel.pvpmode.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import p1xel.pvpmode.Event.PVPModeChangeEvent;
import p1xel.pvpmode.Storage.Data;
import p1xel.pvpmode.Storage.Locale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUIManager implements Listener {

    public static GUIManager instance = new GUIManager();


    public ItemStack getItem(Player player, String modeName) {

        Material mat;
        try {
            mat = Material.matchMaterial(Locale.getGUIMessage("menu." + modeName + ".material"));
        } catch (NullPointerException e) {
            mat = Material.BARRIER;
        }

        String playerName = player.getName();
        String display_name = Locale.getGUIMessage("menu." + modeName + ".display_name");
        String current = Data.getMode(playerName);
        String toggle = "false";
        if (current.equalsIgnoreCase(modeName)) {
            toggle = "true";
        }
        display_name = display_name.replaceAll("%status%", Locale.getGUIMessage("menu.status." + toggle));
        display_name = display_name.replaceAll("%click%", Locale.getGUIMessage("menu.click." + toggle));
        List<String> raw_lore = Locale.get().getStringList("menu." + modeName + ".lore");

        // Other
        int custom_model_data = Locale.get().getInt("menu." + modeName + ".custom_model_data");

        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display_name);
        meta.setCustomModelData(custom_model_data);
        List<String> lore = new ArrayList<>();
        for (String l : raw_lore) {
            l = l.replaceAll("%status%", Locale.getGUIMessage("menu.status." + toggle));
            l = l.replaceAll("%click%", Locale.getGUIMessage("menu.click." + toggle));
            lore.add(Locale.translate(l));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;

    }

    public String matchItem(Player player, ItemStack item) {
        String modeName;
        if (item.getType() == getItem(player,"peace").getType()) {
            modeName = "peace";
            return modeName;
        }
        if (item.getType() == getItem(player,"default").getType()) {
            modeName = "default";
            return modeName;
        }
        if (item.getType() == getItem(player,"insane").getType()) {
            modeName = "insane";
            return modeName;
        }
        modeName = "ERROR";
        return modeName;
    }

    public void createInventory(Player player) {

        if (menus.containsKey(player)) {
            return;
        }

        Inventory inventory = Bukkit.createInventory(player, Locale.get().getInt("menu.size"), Locale.getGUIMessage("menu.title"));

        // Peace Mode
        ItemStack peace = getItem(player,"peace");
        int s1 = Locale.get().getInt("menu.peace.slot");
        inventory.setItem(s1, peace);

        // Default Mode
        ItemStack def = getItem(player,"default");
        int s2 = Locale.get().getInt("menu.default.slot");
        inventory.setItem(s2, def);

        // Insane Mode
        ItemStack insane = getItem(player,"insane");
        int s3 = Locale.get().getInt("menu.insane.slot");
        inventory.setItem(s3, insane);

        menus.put(player, inventory);


    }

    private static HashMap<Player, Inventory> menus = new HashMap<>();

    public static Inventory getPlayerMenu(Player player) {
        return menus.get(player);
    }

    public void setPlayerMenu(Player player, Inventory inventory) {
        if (menus.containsKey(player)) {
            menus.replace(player,inventory);
            return;
        }

        menus.put(player, inventory);
    }

    public boolean isPlayerExist(Player player) {
        return menus.containsKey(player);
    }

    @EventHandler
    public void onGUIClicking(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        if (!isPlayerExist(p)) {
            return;
        }

        Inventory inv = e.getClickedInventory();
        if (getPlayerMenu(p) != inv) {
            return;
        }

        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        String modeName = matchItem(p,item);
        if (modeName.equalsIgnoreCase("ERROR")) {
            return;
        }

        String current = Data.getMode(p.getName());
        if (current.equalsIgnoreCase(modeName)) {
            return;
        }

        PVPModeChangeEvent event = new PVPModeChangeEvent(p, modeName);
        Bukkit.getServer().getPluginManager().callEvent(event);
        updateMode(p,inv ,current, modeName);



    }

    public void updateMode(Player player, Inventory inventory, String fromMode, String toMode) {
        int fromSlot = Locale.get().getInt("menu." + fromMode + ".slot");
        ItemStack from = getItem(player, fromMode);
        inventory.setItem(fromSlot, from);


        int toSlot = Locale.get().getInt("menu." + toMode + ".slot");
        ItemStack to = getItem(player, toMode);
        inventory.setItem(toSlot, to);
    }

    public void updateMode(Player player, String fromMode, String toMode) {

        //Data.setMode(player.getName(), toMode);
        //PVPModeChangeEvent event = new PVPModeChangeEvent(player, toMode);
        //Bukkit.getServer().getPluginManager().callEvent(event);

        Inventory inv = menus.get(player);

        int fromSlot = Locale.get().getInt("menu." + fromMode + ".slot");
        ItemStack from = getItem(player, fromMode);
        inv.setItem(fromSlot, from);


        int toSlot = Locale.get().getInt("menu." + toMode + ".slot");
        ItemStack to = getItem(player, toMode);
        inv.setItem(toSlot, to);

        menus.replace(player, inv);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        if (isPlayerExist(p)) {
            menus.remove(p);
            return;
        }

        createInventory(p);

    }

    public void clearPlayers() {
        menus.clear();
    }



}
