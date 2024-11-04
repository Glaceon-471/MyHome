package com.glaceon_471.myhome;

import com.glaceon_471.myhome.utilities.HomeUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class MyHome extends JavaPlugin {
    private static MyHome Instance;

    public static MyHome getInstance() {
        return Instance;
    }

    public MyHome() {
        Instance = this;
    }

    @Override
    public void onEnable() {
        getCommand("myhome").setExecutor(
            (sender, command, label, args) -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(translatable("player_requests_execution"));
                    return false;
                }
                if (args.length == 0) {
                    sender.sendMessage(translatable("help_command_myhome"));
                    sender.sendMessage(translatable("help_command_set_get_remove"));
                    sender.sendMessage(translatable("help_command_list"));
                    return true;
                }
                String name = "home";
                if (args.length >= 2) name = args[1];
                switch (args[0]) {
                    case "set" -> {
                        Location location = player.getLocation();
                        HomeUtils.setHome(player, location, name);
                        sender.sendMessage(translatable("set_home", location.getBlockX(), location.getBlockY(), location.getBlockZ(), name));
                        return true;
                    }
                    case "go" -> {
                        if (!HomeUtils.hasHome(player, name)) {
                            sender.sendMessage(translatable("non_home", name));
                            return false;
                        }
                        Location pl = player.getLocation();
                        Location location = HomeUtils.getHome(player, name);
                        location.setDirection(pl.getDirection());
                        location.setYaw(pl.getYaw());
                        player.teleport(location);
                        sender.sendMessage(translatable("go_home", name));
                        return true;
                    }
                    case "remove" -> {
                        if (!HomeUtils.hasHome(player, name)) {
                            sender.sendMessage(translatable("non_home", name));
                            return false;
                        }
                        HomeUtils.removeHome(player, name);
                        sender.sendMessage(translatable("remove_home", name));
                        return true;
                    }
                    case "list" -> {
                        for (Map.Entry<String, Location> data : HomeUtils.getHomes(player).entrySet()) {
                            Location location = data.getValue();
                            sender.sendMessage(translatable("home_list", data.getKey(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                        }
                        return true;
                    }
                    default -> {
                        sender.sendMessage(translatable("error_word", args[0]));
                        return false;
                    }
                }
            }
        );
    }

    public TranslatableComponent translatable(String key, Object... args) {
        List<TextComponent> components = new ArrayList<>();
        for (Object obj : args) {
            if (obj instanceof String s) {
                components.add(Component.text(s));
            }
            else if (obj instanceof Boolean b) {
                components.add(Component.text(b));
            }
            else if (obj instanceof Character c) {
                components.add(Component.text(c));
            }
            else if (obj instanceof Double d) {
                components.add(Component.text(d));
            }
            else if (obj instanceof Float f) {
                components.add(Component.text(f));
            }
            else if (obj instanceof Integer i) {
                components.add(Component.text(i));
            }
            else if (obj instanceof Long l) {
                components.add(Component.text(l));
            }
        }
        return Component.translatable("plugin.glaceon471.myhome." + key, components);
    }
}