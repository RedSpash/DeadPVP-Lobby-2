package net.deadpvp.lobby.guiManager;

import net.deadpvp.lobby.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public abstract class PlayerMenu implements InventoryHolder {

    protected Inventory inv;
    protected Player player;

    protected PlayerMenu(Player player) {
        this.player = player;
    }

    protected abstract int getSlots();
    protected abstract String getName();
    public abstract void eventHandler(InventoryClickEvent e) throws IOException;
    protected abstract void setItems();

    public void fillGlass(Material material){
        ItemStack itemStack = new ItemStackBuilder(material).setName("§f").hideAttributes().toItemStack();
        for (int i = 0; i < inv.getSize (); ++i) {
            if (inv.getItem(i) == null) {
                inv.setItem (i, itemStack);
            }
        }
    }

    public void openInv(){
        inv = Bukkit.createInventory(this, getSlots(), getName());

        this.setItems();
        fillGlass(Material.BLUE_STAINED_GLASS_PANE);

        player.openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
