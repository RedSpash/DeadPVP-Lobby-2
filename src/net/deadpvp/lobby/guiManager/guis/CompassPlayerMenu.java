package net.deadpvp.lobby.guiManager.guis;

import net.deadpvp.lobby.guiManager.PlayerMenu;
import net.deadpvp.lobby.server.ServerManager;
import net.deadpvp.lobby.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;


public class CompassPlayerMenu extends PlayerMenu {
    private ServerManager serverManager;

    public CompassPlayerMenu(Player p, ServerManager serverManager) {
        super(p);
        this.serverManager = serverManager;
    }

    @Override
    protected int getSlots() {
        return 36;
    }

    @Override
    protected String getName() {
        return "§2§lSelection du mode de jeu";
    }

    @Override
    public void eventHandler(InventoryClickEvent e) {
        if(e.getCurrentItem() == null)return;

        switch (e.getCurrentItem().getType()){
            case GRASS:
                this.serverManager.sendPlayerToServer(this.player, "creatif");
                break;
            case GRASS_BLOCK:
                this.serverManager.sendPlayerToServer(this.player, "survie");
                break;
            case PAPER:
                e.getWhoClicked().sendMessage("§2§ldeadpvp.fr");
                e.getWhoClicked().closeInventory();
                break;
            default:
                break;
        }
    }

    @Override
    protected void setItems() {

        ItemStackBuilder grass = new ItemStackBuilder(Material.GRASS)
                .setLore ("§bDescription :", "  §7Construisez seul ou entre amis", "  §7le plot de vos rêves !", "§f ", "§6>>> ??? joueurs en créatif !")
                .setName ("§d§lCREATIF").hideAttributes();

        ItemStackBuilder sword = new ItemStackBuilder(Material.DIAMOND_SWORD).setName ("§c§lPVP§9§lSOUP").addEnchant (Enchantment.ARROW_FIRE, 1)
                .setLore ("§bDescription :", "  §7Un mode de jeu classique de DEADPVP ! ", "  §7Combattez vos ennemis dans une map où les soupes",
                        "  §7peuvent vous sauver la vie !","§f ", "§6>>> ??? joueurs en pvpsoup !").hideAttributes();

        ItemStackBuilder barrier = new ItemStackBuilder (Material.BARRIER).setName ("§4Maintenance").setLore (" ");
        ItemStackBuilder paper = new ItemStackBuilder(Material.PAPER).setName ("§d§lSite de DeadPVP").setLore (" ").hideAttributes();

        inv.setItem (11, grass.toItemStack ());
        inv.setItem (13, sword.toItemStack ());
        inv.setItem (15, barrier.toItemStack ());
        inv.setItem (31, paper.toItemStack ());
    }


}
