package net.momirealms.custominstruments.data;

import net.kyori.adventure.bossbar.BossBar;
import net.momirealms.custominstruments.CustomInstruments;
import net.momirealms.custominstruments.utils.Instrument;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    /*
    玩家的乐器布局
    */
    public static HashMap<String, String> DATA;

    /*
    正在演奏的玩家与乐器信息
    */
    public static ConcurrentHashMap<Player, Instrument> onPlaying;
    static {
        onPlaying = new ConcurrentHashMap<>();
    }

    /*
    正在显示的BossBar
    */
    public static ConcurrentHashMap<Player, BossBar> activeBar;
    static {
        activeBar = new ConcurrentHashMap<>();
    }

    /*
    开服时载入布局数据
    */
    public static void loadData() {
        File file = new File(CustomInstruments.instance.getDataFolder(), "data.yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        DATA = new HashMap<>();
        if(data.contains("layout")){
            for (String key : Objects.requireNonNull(data.getConfigurationSection("layout")).getKeys(false)) {
                String layout = data.getString("layout" + "." + key);
                DATA.put(key, layout);
            }
        }
    }

    /*
    保存数据
    */
    public static void saveData(){

        File file = new File(CustomInstruments.instance.getDataFolder(), "data.yml");
        FileConfiguration data;
        data = YamlConfiguration.loadConfiguration(file);

        Set<Map.Entry<String, String>> en = DATA.entrySet();
        for(Map.Entry<String, String> entry : en){
            data.set("layout." + entry.getKey(), entry.getValue());
        }
        try {
            data.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
            CustomInstruments.instance.getLogger().warning("An error occurred when saving data.yml!");
        }
    }
}
