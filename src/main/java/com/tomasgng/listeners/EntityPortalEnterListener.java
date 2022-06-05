package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import com.tomasgng.utils.EntryMode;
import com.tomasgng.utils.PortalManager;
import com.tomasgng.utils.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;

public class EntityPortalEnterListener implements Listener {
    private final PortalManager portalManager = TommyGenerator.getInstance().getPortalManager();
    private final WorldManager worldManager = TommyGenerator.getInstance().getWorldManager();
    private final ArrayList<Player> antiMSGSpam = new ArrayList<>();
    @EventHandler
    public void on(EntityPortalEnterEvent event) {
        if(!(event.getEntity() instanceof Player player))
            return;
        var world = event.getLocation().getWorld();
        var portal = portalManager.getPortal(event.getLocation());

        if(portal == null)
            return;

        var entryMode = worldManager.getEntryMode(portal.to);

        if(entryMode.equals(EntryMode.DENIED) && !player.hasPermission("tommygenerator.entry." + world.getName())) {
            if(!antiMSGSpam.contains(player)) {
                antiMSGSpam.add(player);
                player.sendMessage("§cThe entry for this world is currently denied for you!");
                Bukkit.getScheduler().runTaskLater(TommyGenerator.getInstance(), () -> antiMSGSpam.remove(player), 20*5);
            }
            return;
        }
        player.teleportAsync(portal.to.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
