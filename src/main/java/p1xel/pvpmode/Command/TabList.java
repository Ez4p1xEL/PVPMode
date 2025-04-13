package p1xel.pvpmode.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import p1xel.pvpmode.Storage.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabList implements TabCompleter {

    List<String> args0 = new ArrayList<>();
    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args0.isEmpty()) {
            args0.add("help"); args0.add("info"); args0.add("0"); args0.add("1"); args0.add("2"); args0.add("peace");
            args0.add("default"); args0.add("insane"); args0.add("create"); args0.add("remove"); args0.add("mode"); args0.add("reload");
        }

        List<String> result0 = new ArrayList<>();
        if (args.length == 1) {
            for (String a : args0) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result0.add(a);
                }
            }
            return result0;
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("mode")) {
                List<String> result = new ArrayList<>();
                for (String world : World.get().getKeys(false)) {
                    result.add(world);
                }
                return result;
            }

            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("2")
            || args[0].equalsIgnoreCase("peace") || args[0].equalsIgnoreCase("default") || args[0].equalsIgnoreCase("insane")) {
                List<String> result = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    result.add(player.getName());
                }
                return result;
            }


        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("mode")) {
                List<String> result = Arrays.asList("peace", "default", "insane");
                return result;
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                List<String> result = Collections.singletonList("");
                return result;
            }
        }

        return new ArrayList<>();
    }

}
