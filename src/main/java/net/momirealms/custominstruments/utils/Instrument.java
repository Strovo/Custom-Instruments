package net.momirealms.custominstruments.utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.momirealms.custominstruments.ConfigLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class Instrument {

    private final Material material;
    private List<String> lore;
    private final String name;
    private int cmd;
    private final Key soundKey;
    private float yaw;
    private final Sound.Source type;

    public Instrument(String name, Material material, Key soundKey, Sound.Source type){
        this.name = name;
        this.material = material;
        this.soundKey = soundKey;
        this.type = type;
    }

    public void setLore(List<String> lore){this.lore = lore;}
    public void setCmd(int cmd){this.cmd = cmd;}
    public void setYaw(float yaw){this.yaw = yaw;}

    public List<String> getLore() {return this.lore;}
    public Key getSound(){return this.soundKey;}
    public String getName(){return this.name;}
    public int getCmd(){return this.cmd;}
    public Material getMaterial(){return this.material;}
    public float getYaw(){return this.yaw;}
    public Sound.Source getType(){return this.type;}

    /*
    判断两个物品是否具有相同的特性
    判断 材质，名称，描述，模型值
    */
    public boolean checkItem(ItemStack item) {
        if (item.getType() != this.material) return false;
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.lore();
        if (!itemMeta.hasLore() || !itemMeta.hasDisplayName()) return false;
        if (!Objects.equals(itemMeta.getDisplayName(), this.name)) return false;
        if (this.cmd != 0){
            if (itemMeta.hasCustomModelData()){
                if (this.cmd != itemMeta.getCustomModelData()) return false;
            }else {
                return false;
            }
        }
        return  Objects.equals(itemMeta.getLore(),this.lore);
    }

    /*
    给予玩家乐器
    */
    public static void giveItem(String key, Player player){
        Instrument instrument = ConfigLoader.INSTRUMENTS.get(key);
        ItemStack itemStack = new ItemStack(instrument.material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(instrument.name);
        itemMeta.setLore(instrument.lore);
        if(instrument.cmd != 0){
            itemMeta.setCustomModelData(instrument.cmd);
        }
        itemStack.setItemMeta(itemMeta);
        player.getInventory().addItem(itemStack);
    }
}
