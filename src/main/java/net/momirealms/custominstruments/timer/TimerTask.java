package net.momirealms.custominstruments.timer;

import net.momirealms.custominstruments.AdventureManager;
import net.momirealms.custominstruments.ConfigLoader;
import net.momirealms.custominstruments.data.PlayerData;
import net.momirealms.custominstruments.utils.Instrument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

import static net.momirealms.custominstruments.data.PlayerData.activeBar;
import static net.momirealms.custominstruments.data.PlayerData.onPlaying;

public class TimerTask extends BukkitRunnable {

    @Override
    public void run() {
        for(Map.Entry<Player, Instrument> entry : onPlaying.entrySet()){
            Player player = entry.getKey();
            Instrument instrument = entry.getValue();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (instrument.checkItem(itemStack)){
                if(PlayerData.DATA.get(player.getName()).equalsIgnoreCase("layout_1")){
                    double yaw = player.getLocation().getYaw();
                    double baseYaw = instrument.getYaw();
                    int exp;
                    double angle = ConfigLoader.Config.space_angle/25;
                    if(baseYaw < 0){
                        if(yaw > baseYaw - ConfigLoader.Config.space_angle/2 + 360){
                            exp = (int) ((yaw - baseYaw - 360 - angle/2)/angle);
                        }else if(yaw > baseYaw + 180){
                            exp = -12;
                        }else if(yaw < baseYaw){
                            exp = (int) ((yaw - baseYaw -angle/2)/angle);
                        }else {
                            exp = (int) ((yaw - baseYaw + angle/2)/angle);
                        }
                    }else{
                        if(yaw < baseYaw + ConfigLoader.Config.space_angle/2 - 360){
                            exp = (int) ((yaw - baseYaw + 360 + angle/2)/angle);
                        }else if(yaw < baseYaw - 180){
                            exp = 12;
                        }else if(yaw > baseYaw){
                            exp = (int) ((yaw - baseYaw + angle/2)/angle);
                        }else {
                            exp = (int) ((yaw - baseYaw - angle/2)/angle);
                        }
                    }
                    if(exp > 12){
                        exp = 12;
                    } else if(exp < -12) {
                        exp = -12;
                    }
                    int amount = (exp + 12);
                    StringBuilder bossBar = new StringBuilder(ConfigLoader.Config.ui_1 + ConfigLoader.Config.offset_pointer_1);
                    for (int i = 0; i <= 24; i++){
                        if(i == amount){
                            bossBar.append(ConfigLoader.Config.pointer_1);
                        }else {
                            bossBar.append(ConfigLoader.Config.space_offset_1);
                        }
                    }
                    if (activeBar.get(player) != null){
                        AdventureManager.updateBossBar(player, String.valueOf(bossBar));
                    }else {
                        AdventureManager.sendBossBar(player, String.valueOf(bossBar));
                    }
                }else if(PlayerData.DATA.get(player.getName()).equalsIgnoreCase("layout_2")){
                    double yaw = player.getLocation().getYaw();
                    double baseYaw = instrument.getYaw();
                    int exp;
                    double angle = ConfigLoader.Config.space_angle/30;
                    if(baseYaw < 0) {
                        if (yaw > baseYaw - ConfigLoader.Config.space_angle / 2 + 360) {
                            exp = (int) ((yaw - 360 - baseYaw + ConfigLoader.Config.space_angle / 2) / angle);
                        }else if(yaw > baseYaw + 180){
                            exp = 0;
                        }else {
                            exp = (int) ((yaw - baseYaw + ConfigLoader.Config.space_angle / 2)/angle);
                        }
                    }else {
                        if (yaw < baseYaw + ConfigLoader.Config.space_angle / 2 - 360) {
                            exp = (int) ((yaw + 360 - (baseYaw - ConfigLoader.Config.space_angle / 2)) / angle);
                        }else if(yaw < baseYaw - 180){
                            exp = 29;
                        }else {
                            exp = (int) ((yaw - baseYaw + ConfigLoader.Config.space_angle / 2)/angle);
                        }
                    }
                    if(exp > 29){
                        exp = 29;
                    } else if(exp < 0) {
                        exp = 0;
                    }
                    StringBuilder bossBar = new StringBuilder(ConfigLoader.Config.ui_2 + ConfigLoader.Config.offset_pointer_2);
                    for (int i = 0; i <= 29; i++){
                        if(i == exp){
                            bossBar.append(ConfigLoader.Config.pointer_2);
                        }else {
                            bossBar.append(ConfigLoader.Config.space_offset_2);
                        }
                    }
                    if (activeBar.get(player) != null){
                        AdventureManager.updateBossBar(player, String.valueOf(bossBar));
                    }else {
                        AdventureManager.sendBossBar(player, String.valueOf(bossBar));
                    }
                }
            }else {
                AdventureManager.removeBossBar(player);
                onPlaying.remove(player);
            }
        }
    }
}
