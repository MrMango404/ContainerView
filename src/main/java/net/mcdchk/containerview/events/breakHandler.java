package net.mcdchk.containerview.events;

import net.mcdchk.containerview.ContainerView;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class breakHandler implements Listener {
    ContainerView plugin;

    public breakHandler(ContainerView  plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event){
        try{
            Block bl = event.getBlock();
            if(bl.getBlockData().getMaterial() == Material.DROPPER || bl.getBlockData().getMaterial() == Material.DISPENSER){
                for(Shulker ent : plugin.shulkers){
                    Location loc = event.getBlock().getLocation();
                    loc.setX(event.getBlock().getLocation().getX()+.5);
                    if(plugin.isLocationSame(ent.getLocation(), loc)){
                        ent.setInvulnerable(false);
                        ent.remove();
                        plugin.shulkers.remove(ent);
                        plugin.shulkerList.get(event.getPlayer()).remove(ent);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
