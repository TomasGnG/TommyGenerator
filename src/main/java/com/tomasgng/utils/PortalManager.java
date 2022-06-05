package com.tomasgng.utils;

import com.tomasgng.TommyGenerator;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class PortalManager {

    public File configFile = new File("plugins/TommyGenerator/portals.yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
    private final Map<Player, World> creatingPortal = new HashMap<>();

    public PortalManager() {
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
                save();
            } catch (IOException ignored) {}
        }
    }

    public void reload() {
        cfg = YamlConfiguration.loadConfiguration(configFile);
    }

    public void save() {
        try {
            cfg.save(configFile);
        } catch (IOException e) {
            TommyGenerator.getInstance().getLogger().log(Level.SEVERE, e.getMessage() + "\n" + e.getCause());
        }
        reload();
    }

    public void addPortal(Player player, World from, World to, List<BlockState> blocks) {
        reload();
        // Get all nether portal blocks(NETHER_PORTAL) NOT OBSIDIAN!
        var netherPortalBlocks = new ArrayList<Location>();
        for (var block : blocks) {
            if(block.getType().equals(Material.NETHER_PORTAL))
                netherPortalBlocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
        }
        //Update PortalCount
        cfg.set("PortalCount", cfg.getInt("PortalCount") + 1);
        save();
        //
        var path = "Portals." + cfg.getInt("PortalCount") + ".";
        cfg.set(path + "From", from.getName());
        cfg.set(path + "To", to.getName());
        cfg.set(path + "Blocks", netherPortalBlocks);
        save();
    }

    public void removePortal(Player player, Location location) {
        reload();
        var portal = getPortal(location);
        if(portal != null) {
            cfg.set("Portals." + portal.ID, null);
            cfg.set("PortalCount", cfg.getInt("PortalCount") - 1);
            save();
            if(!cfg.isSet("Portals." + portal.ID))
                player.sendMessage("§aThe portal(ID: " + portal.ID + ") was successfully deleted!");
            else
                player.sendMessage("§cThe portal couldn't be deleted!");
        }
    }

    public void togglePlayersCreatingState(Player player, World destinationWorld, boolean createdPortal) {
        reload();
        if(!isCreatingPortal(player)) {
            creatingPortal.put(player, destinationWorld);
            player.sendMessage(Component.text(" \nTo create the world portal, you need to create a normal nether portal.\nType 'cancel' in the chat if you want to cancel the creation!\n ", DARK_GREEN));
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            return;
        }
        creatingPortal.remove(player);
        if(createdPortal) {
            player.sendMessage(Component.text(" \nThe portal to the world ", GREEN).append(Component.text(destinationWorld.getName(), GREEN)).append(Component.text(" was successfully created! :)\n ", GREEN)));
            player.sendMessage(Component.text("To check a portal use /tg portalinfo !\nTo destroy the portal just break the obsidian next to the portal. Remember that the portal is not secured from breaking by normal players! To avoid that: use something like WorldGuard etc.\n ", RED));
        } else {
            player.sendMessage(Component.text("\nYou left the creating of the portal -> Canceled by user!\n ", RED));
        }
    }
    public TommyPortal getPortal(Location location) {
        reload();
        var blockLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var count = cfg.getInt("PortalCount");
        for (int i = 0; i < count; i++) {
            var id = Integer.parseInt(Objects.requireNonNull(cfg.getConfigurationSection("Portals")).getKeys(false).stream().toList().get(i).substring(0, 1));
            var from = Bukkit.getWorld(Objects.requireNonNull(cfg.getString("Portals." + id + ".From")));
            var to = Bukkit.getWorld(Objects.requireNonNull(cfg.getString("Portals." + id + ".To")));
            var blocks = (List<Location>) cfg.getList("Portals." + id + ".Blocks");
            assert blocks != null;
            for (var searchingLocation : blocks) {
                if(searchingLocation.equals(blockLocation)) {
                    return new TommyPortal(id, from, to, blocks);
                }
            }
        }
        return null;
    }

    public boolean isCreatingPortal(Player player) {
        return creatingPortal.containsKey(player);
    }

    public World getDestinationWorld(Player player) {
        reload();
        return creatingPortal.get(player);
    }

    public int getActivePortalCount() {
        return cfg.getInt("PortalCount");
    }

}
