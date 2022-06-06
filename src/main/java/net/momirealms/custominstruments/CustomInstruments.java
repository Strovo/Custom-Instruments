package net.momirealms.custominstruments;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.momirealms.custominstruments.commands.Execute;
import net.momirealms.custominstruments.commands.TabComplete;
import net.momirealms.custominstruments.data.PlayerData;
import net.momirealms.custominstruments.listeners.PlayerListener;
import net.momirealms.custominstruments.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class CustomInstruments extends JavaPlugin {

    public static JavaPlugin instance;
    public static BukkitAudiences adventure;
    public static Timer timer;

    @Override
    public void onEnable() {

        instance = this;
        adventure = BukkitAudiences.create(this);

        //载入配置文件
        ConfigLoader.Config.ReloadConfig();

        //注册事件，注册指令
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
        Objects.requireNonNull(Bukkit.getPluginCommand("custominstruments")).setExecutor(new Execute());
        Objects.requireNonNull(Bukkit.getPluginCommand("custominstruments")).setTabCompleter(new TabComplete());

        //创建玩家布局文件
        File data = new File(this.getDataFolder(), "data.yml");
        if(!data.exists()){
            try {
                data.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                this.getLogger().warning("An error occurred when generating data.yml");
            }
        }
        //读取玩家布局文件
        PlayerData.loadData();
        //定时发送BossBar
        timer = new Timer();
        //启动成功
        AdventureManager.consoleMessage(Bukkit.getConsoleSender(),"<gradient:#FFBB89:#7B6AE0>[CustomInstruments] Plugin has been enabled! Author: XiaoMoMi</gradient>!");
    }

    @Override
    public void onDisable() {

        //停止定时任务
        if(timer != null){
            Timer.stopTimer(timer.getTaskID());
        }
        //保存缓存数据
        PlayerData.saveData();
        //卸载成功
        AdventureManager.consoleMessage(Bukkit.getConsoleSender(),"<gradient:#FFBB89:#7B6AE0>[CustomInstruments] Plugin has been disabled! Author: XiaoMoMi</gradient>");
        //关闭adventure
        if(adventure != null) {
            adventure.close();
            adventure = null;
        }
    }
}
