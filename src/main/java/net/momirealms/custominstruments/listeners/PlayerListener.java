package net.momirealms.custominstruments.listeners;

import net.kyori.adventure.sound.Sound;
import net.momirealms.custominstruments.ConfigLoader;
import net.momirealms.custominstruments.AdventureManager;
import net.momirealms.custominstruments.data.PlayerData;
import net.momirealms.custominstruments.utils.Instrument;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static net.momirealms.custominstruments.data.PlayerData.onPlaying;

public class PlayerListener implements Listener {

    private final HashMap<Player, Long> coolDown;
    {
        coolDown = new HashMap<>();
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (event.getItem() == null) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR)) return;
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            ItemStack itemStack = event.getItem();
            ConfigLoader.INSTRUMENTS.keySet().forEach(key -> {
                Instrument instrument = ConfigLoader.INSTRUMENTS.get(key);
                if(instrument.checkItem(itemStack)){
                    long time = System.currentTimeMillis();
                    //冷却时间判断
                    if (time - (coolDown.getOrDefault(player, time - ConfigLoader.Config.coolDownTime)) < ConfigLoader.Config.coolDownTime) {
                        AdventureManager.playerMessage(player,ConfigLoader.Config.prefix+ConfigLoader.Config.coolDown);
                        return;
                    }
                    //重置冷却时间
                    coolDown.put(player, time);
                    instrument.setYaw(player.getLocation().getYaw());
                    //将玩家添加进正在演奏玩家的缓存
                    onPlaying.put(player, instrument);
                    //如果玩家没有布局则新增一个布局
                    if(!PlayerData.DATA.containsKey(player.getName())){
                        PlayerData.DATA.put(player.getName(), "layout_1");
                    }
                }
            });
        }else {
            if(onPlaying.containsKey(player)){
                if(PlayerData.DATA.get(player.getName()).equalsIgnoreCase("layout_1")){
                    Instrument instrument = onPlaying.get(player);
                    double yaw = player.getLocation().getYaw();
                    double baseYaw = instrument.getYaw();
                    double exp;
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
                    float pitch = (float) Math.pow(2,(exp/12));
                    Sound sound = Sound.sound(instrument.getSound(), instrument.getType(), ConfigLoader.Config.volume, pitch);
                    AdventureManager.playerSound(player, sound);
                }else if(PlayerData.DATA.get(player.getName()).equalsIgnoreCase("layout_2")){
                    Instrument instrument = onPlaying.get(player);
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
                    float pitch = 1f;
                    if(event.getAction() == Action.RIGHT_CLICK_AIR){
                        switch (exp) {
                            //G 0
                            case 0, 1 -> pitch = 0.5F;
                            //A 2
                            case 2, 3 -> pitch = 0.561231F;
                            //B 4
                            case 4, 5 -> pitch = 0.629961F;
                            //C 5
                            case 6, 7 -> pitch = 0.667420F;
                            //D 7
                            case 8, 9 -> pitch = 0.749154F;
                            //E 9
                            case 10, 11 -> pitch = 0.840896F;
                            //F 10
                            case 12, 13 -> pitch = 0.890899F;
                            //G 12
                            //A 14
                            case 16, 17 -> pitch = 1.122462F;
                            //B 16
                            case 18, 19 -> pitch = 1.259921F;
                            //C 17
                            case 20, 21 -> pitch = 1.334840F;
                            //D 19
                            case 22, 23 -> pitch = 1.498307F;
                            //E 21
                            case 24, 25 -> pitch = 1.681793F;
                            //F 22
                            case 26, 27 -> pitch = 1.781797F;
                            //G 24
                            case 28, 29 -> pitch = 2F;
                        }
                    }else {
                        switch (exp) {
                            //G# 1
                            case 1, 2 -> pitch = 0.529732F;
                            //A# 3
                            case 3, 4 -> pitch = 0.594604F;
                            //C# 6
                            case 7, 8 -> pitch = 0.707107F;
                            //D# 8
                            case 9, 10 -> pitch = 0.793701F;
                            //F# 11
                            case 13, 14 -> pitch = 0.943874F;
                            //G# 13
                            case 15, 16 -> pitch = 1.059463F;
                            //A# 15
                            case 17, 18 -> pitch = 1.189207F;
                            //C# 18
                            case 21, 22 -> pitch = 1.414214F;
                            //D# 20
                            case 23, 24 -> pitch = 1.587401F;
                            //F# 23
                            case 27, 28 -> pitch = 1.887749F;
                            default -> {
                                return;
                            }
                        }
                    }
                    Sound sound = Sound.sound(instrument.getSound(), instrument.getType(), ConfigLoader.Config.volume, pitch);
                    AdventureManager.playerSound(player, sound);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        AdventureManager.removeBossBar(event.getPlayer());
        onPlaying.remove(event.getPlayer());
    }
}
