package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {
    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if(!TommyGenerator.getInstance().getWorldManager().allowedPvP(event.getDamager().getWorld())) {
                event.setCancelled(true);
            }
        }

    }
}
