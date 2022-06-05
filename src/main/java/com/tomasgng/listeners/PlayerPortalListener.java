package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PlayerPortalListener implements Listener {
    @EventHandler
    public void on(PlayerPortalEvent event) {
        var portal = TommyGenerator.getInstance().getPortalManager().getPortal(event.getFrom());
        if(portal != null) {
            event.setCancelled(true);
        }
    }
}
