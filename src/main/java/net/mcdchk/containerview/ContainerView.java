package net.mcdchk.containerview;

import java.util.ArrayList;
import java.util.HashMap;

import net.mcdchk.containerview.commands.guiOpen;
import net.mcdchk.containerview.events.breakHandler;
import net.mcdchk.containerview.events.guiHandler;
import net.mcdchk.containerview.commands.glowContainer;
import net.mcdchk.containerview.events.dropCancel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

public final class ContainerView extends JavaPlugin {
    public ArrayList<Shulker> shulkers = new ArrayList();
    public ArrayList<String> colorsDropper = new ArrayList();
    public ArrayList<String> colorsDispenser = new ArrayList();
    public ArrayList<Integer> teamNames = new ArrayList();
    public HashMap<Player, ArrayList<Shulker>> shulkerList = new HashMap();

    public void onEnable() {
        this.getCommand("cview").setExecutor(new glowContainer(this));
        this.getCommand("cviewc").setExecutor(new guiOpen(this));
        this.getServer().getPluginManager().registerEvents(new guiHandler(this), this);
        this.getServer().getPluginManager().registerEvents(new breakHandler(this), this);
        this.getServer().getPluginManager().registerEvents(new dropCancel(), this);
        this.saveDefaultConfig();
        try {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.getColors();
    }

    public void onDisable() {
    }

    public ArrayList<Block> getBlocks(Player player) {
        ArrayList<Block> returned = new ArrayList<Block>();
        double x = this.getConfig().getDouble("Glow.Radius");
        double y = this.getConfig().getDouble("Glow.Radius");
        double z = this.getConfig().getDouble("Glow.Radius");
        Location loc = player.getLocation();
        Location use = player.getLocation();
        int i = (int)(loc.getY() - y);
        while ((double)i < loc.getY() + y) {
            int b = (int)(loc.getX() - x);
            while ((double)b < loc.getX() + x) {
                int a = (int)(loc.getZ() - z);
                while ((double)a < loc.getZ() + z) {
                    use.setZ(a);
                    use.setX(b);
                    use.setY(i);
                    Block blockMat = use.getBlock();
                    if (blockMat.getBlockData().getMaterial() == Material.DROPPER || blockMat.getBlockData().getMaterial() == Material.DISPENSER) {
                        returned.add(blockMat);
                    }
                    ++a;
                }
                ++b;
            }
            ++i;
        }
        return returned;
    }

    public void startPlayerTimer(final Location loc, final Player player) {
        int delay = this.getConfig().getInt("Glow.Time") * 20;
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.getServer().getPluginManager().getPlugin("ContainerView"), new Runnable(){

            @Override
            public void run() {
                block6: {
                    try {
                        for (Shulker s : ContainerView.this.shulkers) {
                            if (!ContainerView.this.isLocationSame(s.getLocation(), loc).booleanValue()) continue;
                            s.setInvulnerable(false);
                            s.remove();
                            ContainerView.this.shulkers.remove(s);
                        }
                        if (!ContainerView.this.shulkerList.containsKey(player)) break block6;
                        if (ContainerView.this.shulkerList.get(player).isEmpty()) {
                            ContainerView.this.shulkerList.remove(player);
                            break block6;
                        }
                        for (Shulker s : ContainerView.this.shulkerList.get(player)) {
                            if (s.getLocation() == loc) {
                                ContainerView.this.shulkerList.get(player).remove(s);
                            }
                            if (!ContainerView.this.shulkerList.get(player).isEmpty()) continue;
                            ContainerView.this.shulkerList.remove(player);
                            break;
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }, delay);
    }

    public Boolean isLocationSame(Location l1, Location l2) {
        int x1 = (int)l1.getX();
        int y1 = (int)l1.getY();
        int z1 = (int)l1.getZ();
        int x2 = (int)l2.getX();
        int y2 = (int)l2.getY();
        int z2 = (int)l2.getZ();
        if (x1 == x2 && y1 == y2 && z1 == z2) {
            return true;
        }
        return false;
    }

    public void getColors() {
        this.colorsDropper = (ArrayList)this.getConfig().getList("Glow.ColorsDropper");
        this.colorsDispenser = (ArrayList)this.getConfig().getList("Glow.ColorsDispenser");
        System.out.println(this.colorsDispenser);
        System.out.println(this.colorsDropper);
    }
}
