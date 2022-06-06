package net.momirealms.custominstruments;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.momirealms.custominstruments.data.PlayerData.activeBar;

public class AdventureManager {

    /*
    发送控制台消息
    */
    public static void consoleMessage(ConsoleCommandSender sender, String s) {
        Audience au = CustomInstruments.adventure.sender(sender);
        MiniMessage mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(s);
        au.sendMessage(parsed);
    }

    /*
    发送玩家消息
    */
    public static void playerMessage(Player player, String s){
        Audience au = CustomInstruments.adventure.player(player);
        MiniMessage mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(s);
        au.sendMessage(parsed);
    }

    /*
    发送声音
    */
    public static void playerSound(Player player, Sound s){
        player.getLocation().getNearbyEntities(ConfigLoader.Config.volume,ConfigLoader.Config.volume,ConfigLoader.Config.volume).forEach(entity -> {
            if(entity instanceof Player){
                Audience au = CustomInstruments.adventure.player((Player) entity);
                if(entity.hasPermission("custominstruments.hear")){
                    au.playSound(s);
                }
            }
        });
    }

    /*
    BossBar相关
    */
    public static void sendBossBar(Player player, String s){
        Audience au = CustomInstruments.adventure.player(player);
        MiniMessage mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(s);
        BossBar bossBar = BossBar.bossBar(parsed, 1, ConfigLoader.Config.color, BossBar.Overlay.NOTCHED_20);
        au.showBossBar(bossBar);
        activeBar.put(player, bossBar);
    }
    public static void updateBossBar(Player player, String s){
        MiniMessage mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(s);
        activeBar.get(player).name(parsed);
    }
    public static void removeBossBar(Player player){
        Audience au = CustomInstruments.adventure.player(player);
        if(activeBar.get(player) != null){
            au.hideBossBar(activeBar.get(player));
        }
        activeBar.remove(player);
    }
}
