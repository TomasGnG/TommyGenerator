package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void on(InventoryCloseEvent event) {
        if(event.getInventory().getType().equals(InventoryType.ANVIL)) {
            for (int i = 0; i < 3; i++) {
                if(event.getInventory().getItem(i) != null) {
                    var container = Objects.requireNonNull(event.getInventory().getItem(i)).getItemMeta().getPersistentDataContainer();
                    if(containsKey( "worldCreatorInvWorldNameInput-paperItem", container)) {
                        event.getInventory().clear();
                    }
                    if(containsKey("worldEditInvRenameWorldInput-paperItem", container)) {
                        event.getInventory().clear();
                    }
                    if(containsKey("worldEditInvDuplicateWorldInput-paperItem", container)) {
                        event.getInventory().clear();
                    }
                }
            }


        }
    }

    private boolean containsKey(String key, PersistentDataContainer container) {
        return container.has(new NamespacedKey(TommyGenerator.getInstance(), key), PersistentDataType.DOUBLE);
    }

}
