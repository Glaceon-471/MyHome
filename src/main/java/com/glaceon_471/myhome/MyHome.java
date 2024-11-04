package com.glaceon_471.myhome;

import com.glaceon_471.myhome.utilities.HomeUtils;
import jp.dreamingpig.dollyMC.utils.serialization.DStructureEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Map;

public final class MyHome extends JavaPlugin {
    private static MyHome Instance;

    public static MyHome getInstance() {
        return Instance;
    }

    private DStructureEntry PluginTranslation;

    public MyHome() {
        Instance = this;
    }

    @Override
    public void onEnable() {
        PluginTranslation = DStructureEntry.openStructure(this, "Translation");

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

    private final Map<String, String> DefaultString = Map.of(
        "player_requests_execution", "§cプレイヤーのみ実行可能です",
        "help_command_myhome", "§a/myhome",
        "help_command_set_get_remove", "§a/myhome <set|go|remove> [name]",
        "help_command_list", "§a/myhome list",
        "set_home", "§a%d, %d, %dを%sとして登録しました",
        "go_home", "§a%sにテレポートしました",
        "remove_home", "§a%sを削除しました",
        "home_list", "§a%s : %d, %d, %d",
        "non_home", "§c%sという場所は存在しません",
        "error_word", "§c%sは不明な設定です"
    );
    public Component translatable(String key, Object... args) {
        return Component.text(String.format(PluginTranslation.getString(
            key,
            DefaultString.get(key)
        ).get(), args));
    }
}
