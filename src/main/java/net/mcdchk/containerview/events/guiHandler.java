package net.mcdchk.containerview.events;

import net.mcdchk.containerview.ContainerView;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class guiHandler implements Listener {
    ContainerView plugin;

    public guiHandler(ContainerView plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {

        String Dropper = plugin.getConfig().getString("Messages.GUITitleDropper");
        String Dispenser = plugin.getConfig().getString("Messages.GUITitleDispenser");

        if (event.getView().getTitle().equals(Dropper)) {
            event.setCancelled(true);
            if (event.getView().getTitle().equals(Dispenser)) {
                event.setCancelled(true);
            }
        }
    }
}


