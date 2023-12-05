package net.deadpvp.lobby.menu;

import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MainMenu {

    public static final String FILE_CONFIGURATION_KEY = "menu.";
    public static final String MULTIPLICATION_SYMBOL = "\\*";
    private final Inventory inventory;
    private final int size;
    private final String title;
    private final Material fillMaterial;
    private final FileConfiguration fileConfiguration;

    public MainMenu(Configuration configuration) {
        this.fileConfiguration = configuration.getFileConfiguration();

        this.size = this.fileConfiguration.getInt(FILE_CONFIGURATION_KEY +"lines",6)*9;

        this.title = this.fileConfiguration.getString(FILE_CONFIGURATION_KEY +"title","");
        this.inventory = Bukkit.createInventory(null,this.size,this.title);

        this.fillMaterial = Material.valueOf(this.fileConfiguration.getString(FILE_CONFIGURATION_KEY+"fill_inventory", "AIR").toUpperCase().replace(" ","_"));

        this.fillInventory(this.fillMaterial);

        this.addMainContent();
    }


    private void addMainContent() {
        this.fileConfiguration.getConfigurationSection(FILE_CONFIGURATION_KEY+"content").getKeys(false).forEach(position ->{
            String path = FILE_CONFIGURATION_KEY+"content."+ position+".";
            try{
                Material material = Material.valueOf(this.fileConfiguration.getString(path+"type","BEDROCK").toUpperCase());
                String name = this.fileConfiguration.getString(path+"name","§cAucun nom");
                List<?> lore = this.fileConfiguration.getList(path+"lore");
                boolean isEnchanted = this.fileConfiguration.getBoolean(path+"isEnchanted");

                ItemStackBuilder itemStackBuilder = new ItemStackBuilder(material)
                        .setName(name)
                        .setLore((List<String>) lore)
                        .hideAttributes();

                if(isEnchanted){
                    itemStackBuilder.addEnchant(Enchantment.ARROW_FIRE,1);
                }

                this.inventory.setItem(Integer.parseInt(position),itemStackBuilder.toItemStack());
            }catch (Exception e){
                Bukkit.getConsoleSender().sendMessage("§cUne erreur est survenue sur l'élément à la position "+position+" du menu. ("+e.getMessage()+")");
            }
        });
    }

    private void fillInventory(Material fillMaterial) {
        ItemStack itemStack = new ItemStackBuilder(fillMaterial).setName("§f").hideAttributes().toItemStack();
        for(int i = 0; i < this.size; i++){
            this.inventory.setItem(i,itemStack);
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
