package net.momirealms.custominstruments;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.momirealms.custominstruments.utils.Instrument;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class ConfigLoader {

    public static class Config{

        public static int coolDownTime;
        public static int volume;

        public static double space_angle;

        public static String prefix;
        public static String coolDown;
        public static String reload;
        public static String noConsole;
        public static String getItem;
        public static String giveItem;
        public static String notOnline;
        public static String noInstrument;
        public static String ui_1;
        public static String pointer_1;
        public static String offset_pointer_1;
        public static String space_offset_1;
        public static String ui_2;
        public static String pointer_2;
        public static String offset_pointer_2;
        public static String space_offset_2;
        public static String noPerm;
        public static String toggle;

        public static BossBar.Color color;

        /*
        重载插件
        */
        public static void ReloadConfig(){

            CustomInstruments.instance.saveDefaultConfig();
            CustomInstruments.instance.reloadConfig();
            FileConfiguration config = CustomInstruments.instance.getConfig();
            ItemsLoad();

            prefix = config.getString("messages.prefix");
            coolDown = config.getString("messages.cooldown");
            reload = config.getString("messages.reload");
            noConsole = config.getString("messages.no-console");
            getItem = config.getString("messages.get-item");
            giveItem = config.getString("messages.give-item");
            notOnline = config.getString("messages.not-online");
            noInstrument = config.getString("messages.no-instrument");
            noPerm = config.getString("messages.no-perm");
            toggle = config.getString("messages.toggle");

            color = BossBar.Color.valueOf(config.getString("config.bossbar"));
            ui_1 = config.getString("layout-1.ui");
            pointer_1 = config.getString("layout-1.pointer");
            offset_pointer_1 = config.getString("layout-1.offset_pointer");
            space_offset_1 = config.getString("layout-1.space_offset");

            ui_2 = config.getString("layout-2.ui");
            pointer_2 = config.getString("layout-2.pointer");
            offset_pointer_2 = config.getString("layout-2.offset_pointer");
            space_offset_2 = config.getString("layout-2.space_offset");

            coolDownTime = config.getInt("config.cooldown");
            volume = config.getInt("config.volume");

            space_angle = config.getDouble("config.space_angle");
        }
    }

    public static HashMap<String, Instrument> INSTRUMENTS;
    static {
        INSTRUMENTS = new HashMap<>();
    }

    private static YamlConfiguration getConfig(String configName) {

        File file = new File(CustomInstruments.instance.getDataFolder(), configName);
        //文件不存在则生成默认配置
        if (!file.exists()) {
            CustomInstruments.instance.saveResource(configName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    /*
    加载乐器数据
    */
    public static void ItemsLoad() {

        try {
            INSTRUMENTS.clear();
            YamlConfiguration itemConfig = ConfigLoader.getConfig("items.yml");
            Set<String> keys = Objects.requireNonNull(itemConfig.getConfigurationSection("items")).getKeys(false);
            keys.forEach(key -> {

                Material material = Material.valueOf(itemConfig.getString("items." + key + ".material"));
                String name = Objects.requireNonNull(itemConfig.getString("items." + key + ".display.name")).replaceAll("&","§");
                String[] split = itemConfig.getString("items."+key+".sound").split(":");
                Key soundKey = Key.key(split[0], split[1]);
                Sound.Source type = Sound.Source.valueOf(Objects.requireNonNull(itemConfig.getString("items." + key + ".type")).toUpperCase());

                Instrument instrument = new Instrument(name, material, soundKey, type);

                if (itemConfig.contains("items." + key + ".custom-model-data")){
                    instrument.setCmd(itemConfig.getInt("items." + key + ".custom-model-data"));
                }else {
                    instrument.setCmd(0);
                }
                if (itemConfig.contains("items."+key+".display.lore")){
                    List<String> lore = itemConfig.getStringList("items." + key + ".display.lore");
                    for (int i = 0; i < lore.size(); i++) {
                        lore.set(i, lore.get(i).replaceAll("&", "§"));
                    }
                    instrument.setLore(lore);
                }
                INSTRUMENTS.put(key, instrument);
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            CustomInstruments.instance.getLogger().warning("Failed to load items.yml!");
        }
    }
}
