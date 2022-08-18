package net.mcdchk.containerview.commands;

import java.util.ArrayList;
import java.util.Random;

import net.mcdchk.containerview.ContainerView;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class glowContainer
        implements CommandExecutor {
    ContainerView plugin;

    public glowContainer(ContainerView plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)((Object)sender);
            ArrayList<Block> lottery = new ArrayList<Block>(this.plugin.getBlocks(player));
            if (!lottery.isEmpty()) {
                ArrayList<Shulker> TBH = new ArrayList<Shulker>();
                Integer counter = 0;
                player.sendMessage(this.plugin.getConfig().getString("Messages.Spacer"));
                for (Block block : lottery) {
                    TextComponent msg;
                    Team team;
                    for (Shulker sh : this.plugin.shulkers) {
                        if (block.getLocation() != sh.getLocation()) continue;
                        lottery.remove(block);
                    }
                    if (lottery.isEmpty()) {
                        player.sendMessage(this.plugin.getConfig().getString("Messages.AlreadyGlowingAroundYou"));
                        continue;
                    }
                    if (counter >= this.plugin.getConfig().getInt("Glow.MaxContainers")) break;
                    Location loc = block.getLocation();
                    Shulker spawnShulker = (Shulker)((Object)loc.getWorld().spawnEntity(loc, EntityType.SHULKER));
                    spawnShulker.setGlowing(true);
                    spawnShulker.setInvulnerable(true);
                    spawnShulker.setAI(false);
                    spawnShulker.setInvisible(true);
                    this.plugin.shulkers.add(spawnShulker);
                    this.plugin.startPlayerTimer(spawnShulker.getLocation(), player);
                    Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
                    TBH.add(spawnShulker);
                    if (block.getBlockData().getMaterial() == Material.DROPPER) {
                        team = board.registerNewTeam("DropperTeam" + this.makeName());
                        team.setDisplayName(ChatColor.BLUE + "Blue");
                        team.setColor(ChatColor.valueOf(this.plugin.colorsDropper.get(counter)));
                        team.addEntry(spawnShulker.getUniqueId().toString());
                        msg = new TextComponent(this.deCypherConfig(spawnShulker, net.md_5.bungee.api.ChatColor.valueOf(this.plugin.colorsDispenser.get(counter))));
                        msg.setColor(net.md_5.bungee.api.ChatColor.valueOf(this.plugin.colorsDropper.get(counter)));
                        msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cviewc " + TBH.size()));
                        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.getConfig().getString("Messages.HoverText")).color(net.md_5.bungee.api.ChatColor.GRAY).create()));
                        player.spigot().sendMessage(msg);
                    } else {
                        team = board.registerNewTeam("DispenserTeam" + this.makeName());
                        team.setDisplayName(ChatColor.RED + "Red");
                        team.setColor(ChatColor.valueOf(this.plugin.colorsDispenser.get(counter)));
                        team.addEntry(spawnShulker.getUniqueId().toString());
                        msg = new TextComponent(this.deCypherConfig(spawnShulker, net.md_5.bungee.api.ChatColor.valueOf(this.plugin.colorsDispenser.get(counter))));
                        msg.setColor(net.md_5.bungee.api.ChatColor.valueOf(this.plugin.colorsDispenser.get(counter)));
                        msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cviewc " + TBH.size()));
                        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.plugin.getConfig().getString("Messages.HoverText")).color(net.md_5.bungee.api.ChatColor.GRAY).create()));
                        player.spigot().sendMessage(msg);
                    }
                    counter = counter + 1;
                }
                if (!TBH.isEmpty()) {
                    this.plugin.shulkerList.put(player, TBH);
                }
            } else {
                player.sendMessage(this.plugin.getConfig().getString("Messages.NotFound"));
            }
        }
        return false;
    }

    public String deCypherConfig(Shulker s, net.md_5.bungee.api.ChatColor cc) {
        int x = (int)s.getLocation().getX();
        int y = (int)s.getLocation().getY();
        int z = (int)s.getLocation().getZ();
        String returned = "";
        String[] split = this.plugin.getConfig().getString("Messages.ContainerFound").split("-");
        returned = cc + split[0] + " " + net.md_5.bungee.api.ChatColor.WHITE + split[1];
        returned = returned.replace("%X%", "" + x);
        returned = returned.replace("%Y%", "" + y);
        returned = returned.replace("%Z%", "" + z);
        return returned;
    }

    public Integer makeName() {
        Random random = new Random();
        int teamCode = random.nextInt(100000);
        if (!this.plugin.teamNames.contains(teamCode)) {
            return teamCode;
        }
        this.makeName();
        return null;
    }
}
