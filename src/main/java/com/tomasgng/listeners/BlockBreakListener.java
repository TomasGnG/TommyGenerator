package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void on(BlockBreakEvent event) {
        var player = event.getPlayer();
        var world = player.getWorld();
        if(event.getBlock().getType().equals(Material.OBSIDIAN) || event.getBlock().getType().equals(Material.NETHER_PORTAL)) {
            BlockFace[] blockFaces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            for (var face : blockFaces) {
                var currentBlock = event.getBlock();
                if(currentBlock.getRelative(face).getType().equals(Material.NETHER_PORTAL)) {
                    TommyGenerator.getInstance().getPortalManager().removePortal(player, currentBlock.getRelative(face).getLocation());
                }
            }
        }
    }
}
