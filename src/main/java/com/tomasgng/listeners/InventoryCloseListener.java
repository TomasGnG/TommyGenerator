package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataType;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void on(InventoryCloseEvent event) {
        if(event.getInventory().getType().equals(InventoryType.ANVIL)) {
            for (int i = 0; i < 3; i++) {
                if(event.getInventory().getItem(i) != null) {
                    if(event.getInventory().getItem(i).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInvWorldNameInput-paperItem"), PersistentDataType.DOUBLE)) {
                        event.getInventory().clear();
                    }
                }
            }


        }
    }

}
