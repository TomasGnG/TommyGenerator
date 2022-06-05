package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalCreateListener implements Listener {
    @EventHandler
    public void on(PortalCreateEvent event) {
        var player = (Player) event.getEntity();
        var portalManager = TommyGenerator.getInstance().getPortalManager();
        if(portalManager.isCreatingPortal(player)) {
            portalManager.addPortal(player, event.getWorld(), portalManager.getDestinationWorld(player), event.getBlocks());
            portalManager.togglePlayersCreatingState(player, portalManager.getDestinationWorld(player), true);
        }
    }
}
