package net.mcdchk.containerview.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class dropCancel implements Listener {
    @EventHandler
    public void onShulkerDeath(EntityDeathEvent e) {
        if (e.getEntity().isGlowing()) {
            e.getDrops().clear();
            e.setDroppedExp(0);
        }
    }
}
