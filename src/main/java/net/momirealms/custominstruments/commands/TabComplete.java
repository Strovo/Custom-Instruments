package net.momirealms.custominstruments.commands;

import net.momirealms.custominstruments.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {
    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(sender.hasPermission("custominstruments.admin")){
            if(1 == args.length){
                return Arrays.asList("reload","instruments","toggle");
            }
            if(2 == args.length && args[0].equalsIgnoreCase("instruments")){
                return Arrays.asList("get","give");
            }
            if(3 == args.length && args[1].equalsIgnoreCase("get") && args[0].equalsIgnoreCase("instruments")){
                return items();
            }
            else if(3 == args.length && args[1].equalsIgnoreCase("give")){
                return online_players();
            }
            if(4 == args.length && args[1].equalsIgnoreCase("give") && args[0].equalsIgnoreCase("instruments")){
                return items();
            }
        }else {
            if(1 == args.length){
                return Collections.singletonList("toggle");
            }
        }
        return null;
    }

    /*
    获取在线玩家列表
    */
    private static List<String> online_players(){
        List<String> online = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach((player -> online.add(player.getDisplayName())));
        return online;
    }

    /*
    获取缓存中的乐器列表
    */
    private static List<String> items(){
        return new ArrayList<>(ConfigLoader.INSTRUMENTS.keySet());
    }
}
