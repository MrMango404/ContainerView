package net.mcdchk.containerview.commands;

import net.mcdchk.containerview.ContainerView;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class guiOpen implements CommandExecutor {
    ContainerView plugin;

    public guiOpen(ContainerView plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try{
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(plugin.shulkerList.containsKey(player)){
                    int index = Integer.parseInt(args[0])-1;
                    onPlayerMessageClick(plugin.shulkerList.get(player).get(index).getLocation(), player);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public void onPlayerMessageClick(Location cl, Player player){
        try{
            Location pl = player.getLocation();
            if(pl.distance(cl) <= plugin.getConfig().getInt("Glow.Radius")){
                Block bl = cl.getBlock();
                if(bl.getState() instanceof Dropper){
                    Dropper drop = (Dropper) bl.getState();
                    Inventory inv = Bukkit.createInventory(player, InventoryType.DROPPER, plugin.getConfig().getString("Messages.GUITitleDropper"));
                    inv.setContents(drop.getInventory().getContents());
                    player.openInventory(inv);
                    player.sendMessage(plugin.getConfig().getString("Messages.OpenedDropper"));
                }else if(bl.getState() instanceof Dispenser){
                    Dispenser disp = (Dispenser) bl.getState();
                    Inventory inv = Bukkit.createInventory(player, InventoryType.DISPENSER, plugin.getConfig().getString("Messages.GUITitleDispenser"));
                    inv.setContents(disp.getInventory().getContents());
                    player.openInventory(inv);
                    player.sendMessage(plugin.getConfig().getString("Messages.OpenedDispenser"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
