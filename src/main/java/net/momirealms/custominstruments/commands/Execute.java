package net.momirealms.custominstruments.commands;

import net.momirealms.custominstruments.ConfigLoader;
import net.momirealms.custominstruments.AdventureManager;
import net.momirealms.custominstruments.data.PlayerData;
import net.momirealms.custominstruments.utils.Instrument;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;

public class Execute implements CommandExecutor {
    /*
    代码即注释
    */
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) return true;
        if(args[0].equalsIgnoreCase("toggle")){
            if(sender instanceof Player){
                if(PlayerData.DATA.containsKey(sender.getName())){
                    if(PlayerData.DATA.get(sender.getName()).equalsIgnoreCase("layout_1")){
                        PlayerData.DATA.put(sender.getName(), "layout_2");
                        AdventureManager.playerMessage((Player) sender, ConfigLoader.Config.prefix + ConfigLoader.Config.toggle);
                    }
                    else if(PlayerData.DATA.get(sender.getName()).equalsIgnoreCase("layout_2")){
                        PlayerData.DATA.put(sender.getName(), "layout_1");
                        AdventureManager.playerMessage((Player) sender, ConfigLoader.Config.prefix + ConfigLoader.Config.toggle);
                    }
                }
                else {
                    PlayerData.DATA.put(sender.getName(), "layout_1");
                    AdventureManager.playerMessage((Player) sender, ConfigLoader.Config.prefix + ConfigLoader.Config.toggle);
                }
            }
            else {
                AdventureManager.consoleMessage(Bukkit.getConsoleSender(), ConfigLoader.Config.prefix + ConfigLoader.Config.noConsole);
            }
            return true;
        }
        if(!sender.hasPermission("custominstruments.admin")){
            AdventureManager.playerMessage((Player) sender,ConfigLoader.Config.prefix + ConfigLoader.Config.noPerm );
            return true;
        }
        if(args[0].equalsIgnoreCase("reload")){
            ConfigLoader.Config.ReloadConfig();
            if(sender instanceof Player){
                AdventureManager.playerMessage((Player) sender, ConfigLoader.Config.prefix + ConfigLoader.Config.reload);
            }
            else {
                AdventureManager.consoleMessage(Bukkit.getConsoleSender(), ConfigLoader.Config.prefix + ConfigLoader.Config.reload);
            }
            return true;
        }
        if(args.length > 2){
            if(args[0].equalsIgnoreCase("instruments")){
                if(args[1].equalsIgnoreCase("get")){
                    if(sender instanceof Player){
                        Instrument.giveItem(args[2], (Player) sender);
                        AdventureManager.playerMessage((Player) sender, ConfigLoader.Config.prefix + ConfigLoader.Config.getItem.replace("{item}",args[2]));
                    }
                    else {
                        AdventureManager.consoleMessage(Bukkit.getConsoleSender(), ConfigLoader.Config.prefix + ConfigLoader.Config.noConsole);
                    }
                    return true;
                }
                if(args[1].equalsIgnoreCase("give") && args.length > 3){
                    Player player = Bukkit.getPlayer(args[2]);
                    if (player != null){
                        if(ConfigLoader.INSTRUMENTS.containsKey(args[3])){
                            if(sender instanceof Player){
                                Instrument.giveItem(args[3],player);
                                AdventureManager.playerMessage((Player) sender, ConfigLoader.Config.prefix + ConfigLoader.Config.giveItem.replace("{player}",args[2]).replace("{item}",args[3]));
                            }
                            else {
                                Instrument.giveItem(args[3],player);
                                AdventureManager.consoleMessage(Bukkit.getConsoleSender(), ConfigLoader.Config.prefix + ConfigLoader.Config.giveItem.replace("{player}",args[2]).replace("{item}",args[3]));
                            }
                        }
                        else {
                            if(sender instanceof Player){
                                AdventureManager.playerMessage((Player) sender, ConfigLoader.Config.prefix + ConfigLoader.Config.noInstrument);
                            }
                            else {
                                AdventureManager.consoleMessage(Bukkit.getConsoleSender(), ConfigLoader.Config.prefix + ConfigLoader.Config.noInstrument);
                            }
                        }
                    }
                    else {
                        if(sender instanceof Player){
                            AdventureManager.playerMessage((Player) sender,ConfigLoader.Config.prefix + ConfigLoader.Config.notOnline.replace("{player}",args[2]));
                        }
                        else {
                            AdventureManager.consoleMessage(Bukkit.getConsoleSender(), ConfigLoader.Config.prefix + ConfigLoader.Config.notOnline.replace("{player}",args[2]));
                        }
                    }
                    return true;
                }
            }
        }
        return true;
    }
}
