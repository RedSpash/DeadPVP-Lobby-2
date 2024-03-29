package net.deadpvp.lobby.menu;

import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.menu.commands.*;
import net.deadpvp.lobby.menu.commands.InventoryCommand;
import net.deadpvp.lobby.server.BungeeManager;
import net.deadpvp.lobby.utils.ItemStackBuilder;
import net.deadpvp.lobby.variables.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainMenu {

    public static final String FILE_CONFIGURATION_KEY = "menu.";
    private final VariableManager variableManager;
    private Inventory inventory;
    private int size;
    private String title;
    private FileConfiguration fileConfiguration;
    private final HashMap<String, ArrayList<Command>> actions;
    private final BungeeManager bungeeManager;

    public MainMenu(Configuration configuration, BungeeManager bungeeManager, VariableManager variableManager) {
        this.bungeeManager = bungeeManager;
        this.actions = new HashMap<>();
        this.variableManager = variableManager;

        this.reloadData(configuration);
    }


    private void addMainContent() {
        this.inventory.clear();
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

                ItemStack itemStack = itemStackBuilder.toItemStack();
                this.inventory.setItem(Integer.parseInt(position),itemStack);
                ArrayList<Command> commands = new ArrayList<>();
                if(this.fileConfiguration.isSet(path+"actions")){
                    this.fileConfiguration.getConfigurationSection(path+"actions").getKeys(false).forEach(action ->{
                        String actionPath = path+"actions."+action;
                        Command command;
                        switch (action.toLowerCase()){
                            case "sendto"->
                                    command = new SendCommand(this.bungeeManager,this.fileConfiguration.getString(actionPath,""));
                            case "sendmessage"->
                                    command = new SendMessageCommand(this.fileConfiguration.getString(actionPath,""));
                            case "execute"->
                                    command = new ExecuteCommand(this.fileConfiguration.getString(actionPath,""));
                            case "teleportto"->{
                                double x = this.fileConfiguration.getDouble(actionPath+".x",0.0);
                                double y = this.fileConfiguration.getDouble(actionPath+".y",100.0);
                                double z = this.fileConfiguration.getDouble(actionPath+".z",0.0);
                                float pitch = (float) this.fileConfiguration.getDouble(actionPath+".pitch",0.0);
                                float yaw = (float) this.fileConfiguration.getDouble(actionPath+".yaw",0.0);
                                String world = this.fileConfiguration.getString(actionPath+".world",null);
                                command = new TeleportCommand(x,y,z,pitch,yaw,world);
                            }
                            case "inventory"-> command = new InventoryCommand(this.fileConfiguration.getString(actionPath,""));
                            default ->
                                command = new SendMessageCommand("§c§lCet item n'est pas configuré correctement!");

                        }
                        commands.add(command);
                    });
                }
                this.actions.put(name,commands);
            }catch (Exception e){
                Bukkit.getConsoleSender().sendMessage("§cUne erreur est survenue sur l'élément à la position "+position+" du menu. ("+e.getMessage()+")");
            }
        });
    }

    private void fillInventory(Material fillMaterial) {
        if(fillMaterial != Material.AIR){
            ItemStack itemStack = new ItemStackBuilder(fillMaterial).setName("§f").hideAttributes().toItemStack();
            for(int i = 0; i < this.size; i++){
                this.inventory.setItem(i,itemStack);
            }
        }
    }

    public void eventHandler(InventoryClickEvent e){
        ItemStack itemStack = e.getCurrentItem();
        if(itemStack == null)return;

        for(String name : this.actions.keySet()){
            if(itemStack.hasItemMeta() &&
                    itemStack.getItemMeta().hasDisplayName() &&
                    itemStack.getItemMeta().getDisplayName().equals(name)){
                    for(Command command : this.actions.get(name)){
                        command.execute((Player) e.getWhoClicked());
                    }
                    break;

            }
        }
    }

    public Inventory getInventory(Player p) {
        Inventory inventory1 = Bukkit.createInventory(null,this.size,this.title);

        for(int i =0; i<this.inventory.getSize(); i++){
            ItemStack itemStack = this.inventory.getItem(i);
            if(itemStack != null){
                itemStack = itemStack.clone();
                if(itemStack.hasItemMeta()){
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if(itemMeta instanceof SkullMeta skullMeta){
                        skullMeta.setOwnerProfile(p.getPlayerProfile());
                        itemStack.setItemMeta(skullMeta);
                        itemMeta = itemStack.getItemMeta();
                    }
                    if(itemMeta.hasDisplayName() && (this.actions.containsKey(itemMeta.getDisplayName()) && (itemMeta.hasLore()))){
                        ArrayList<String> lore = new ArrayList<>(itemMeta.getLore());
                        ArrayList<String> editedLore = new ArrayList<>();
                        for(String line : lore){
                            editedLore.add(this.variableManager.getStringWithReplacedVariables(line,p));
                        }
                        itemMeta.setLore(editedLore);
                        itemStack.setItemMeta(itemMeta);
                    }
                }
                inventory1.setItem(i,itemStack);
            }
        }
        return inventory1;
    }

    public String getTitle() {
        return this.title;
    }

    public void reloadData(Configuration configuration) {
        this.fileConfiguration = configuration.getFileConfiguration();

        this.size = this.fileConfiguration.getInt(FILE_CONFIGURATION_KEY +"lines",6)*9;

        this.title = this.fileConfiguration.getString(FILE_CONFIGURATION_KEY +"title","");
        this.inventory = Bukkit.createInventory(null,this.size,this.title);

        Material fillMaterial = Material.valueOf(this.fileConfiguration.getString(FILE_CONFIGURATION_KEY+"fill_inventory", "AIR").toUpperCase().replace(" ","_"));

        this.fillInventory(fillMaterial);

        this.addMainContent();
    }
}
