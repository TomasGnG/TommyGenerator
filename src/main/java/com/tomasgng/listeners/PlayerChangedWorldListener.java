package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorldListener implements Listener {
    @EventHandler
    public void on(PlayerChangedWorldEvent event) {
        var worldManager = TommyGenerator.getInstance().getWorldManager();
        if(!worldManager.getGameMode(event.getPlayer().getWorld()).equals("DISABLED") && !worldManager.getGameMode(event.getPlayer().getWorld()).equals("NOT SET")) {
            if(!event.getPlayer().hasPermission("tommygenerator.worldgamemode.bypass")) {
                event.getPlayer().setGameMode(GameMode.valueOf(worldManager.getGameMode(event.getPlayer().getWorld())));
            }
        }
    }
}
