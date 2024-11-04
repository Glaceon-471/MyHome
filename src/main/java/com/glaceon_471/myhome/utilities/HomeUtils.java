package com.glaceon_471.myhome.utilities;

import com.glaceon_471.myhome.MyHome;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class HomeUtils {
    public static void setHome(Player player, Location location, String name) {
        DStructureEntry config = getHomeConfig(player);
        DStructureEntry home = config.getStructure(name);
        home.getString("world", Bukkit.getServerName()).set(location.getWorld().getName());
        home.getDouble("x", 0).set(location.getBlockX() + 0.5);
        home.getDouble("y", 0).set((double)location.getBlockY());
        home.getDouble("z", 0).set(location.getBlockZ() + 0.5);
        config.save();
    }

    public static boolean hasHome(Player player, String name) {
        return getHomeConfig(player).contains(name);
    }

    public static Location getHome(Player player, String name) {
        DStructureEntry config = getHomeConfig(player);
        DStructureEntry home = config.getStructure(name);
        return new Location(
            Bukkit.getWorld(home.getString("world", Bukkit.getServerName()).get()),
            home.getDouble("x", 0).get(),
            home.getDouble("y", 0).get(),
            home.getDouble("z", 0).get()
        );
    }

    public static void removeHome(Player player, String name) {
        DStructureEntry config = getHomeConfig(player);
        config.getSection().set(name, null);
        config.save();
    }

    public static Map<String, Location> getHomes(Player player) {
        DStructureEntry config = getHomeConfig(player);
        ConfigurationSection section = config.getSection();
        Map<String, Location> data = new HashMap<>();
        for (String key : section.getKeys(false)) {
            data.put(key, section.getLocation(key, player.getLocation()));
        }
        return data;
    }

    private static DStructureEntry getHomeConfig(Player player) {
        return DStructureEntry.openStructure(MyHome.getInstance(), player.getUniqueId().toString());
    }
}
