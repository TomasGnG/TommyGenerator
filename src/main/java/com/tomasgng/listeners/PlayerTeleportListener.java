package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {
    @EventHandler
    public void on(PlayerTeleportEvent event) {
        var destinationWorld = event.getTo().getWorld();
        var player = event.getPlayer();
        var worldManager = TommyGenerator.getInstance().getWorldManager();

        if(worldManager.getEntryMode(destinationWorld).equals("DENIED") && !player.hasPermission("tommygenerator.entry." + destinationWorld.getName())) {
            event.setCancelled(true);
            player.sendMessage("§cThe entry for this world is currently denied for you!");
        }
    }
}
