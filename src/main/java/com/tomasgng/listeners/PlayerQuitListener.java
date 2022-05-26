package com.tomasgng.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void on(PlayerQuitEvent event) {
        var addedEffects = PlayerChangedWorldListener.addedEffects;
        var player = event.getPlayer();
        if(addedEffects.containsKey(player)) {
            for (var effect : addedEffects.get(player)) {
                player.removePotionEffect(effect.getType());
            }
        }
    }
}
