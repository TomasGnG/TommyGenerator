package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataType;

public class InventoryClickListener implements Listener {

    // ANVIL INV
    /*@EventHandler
    public void onAnvil(PrepareAnvilEvent event) {
        if(event.getInventory().getItem(0).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInvWorldNameInput-paperItem"), PersistentDataType.DOUBLE)) {
            if(event.getResult() != null || event.getResult().getItemMeta() != null) {
                event.getViewers().stream().findFirst().get().closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
                TommyGenerator.getInstance().getGuiManager().openWorldCreatorInventory((Player) event.getViewers().stream().findFirst().get(), event.getResult().displayName().toString());
            }
        }
    }*/



    @EventHandler
    public void on(InventoryClickEvent event) {
        var inventoryView = event.getView();
        var player = (Player) event.getWhoClicked();

        if(event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
            var persistentContainer = event.getCurrentItem().getItemMeta().getPersistentDataContainer();
            var world = Bukkit.getWorld(inventoryView.getTitle().substring(2));
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "mainMenuItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                TommyGenerator.getInstance().getGuiManager().openMainInventory(player);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-tpItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                TommyGenerator.getInstance().getWorldManager().teleportToWorldSpawn(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "tg-defaultGlassItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-lockTimeItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                TommyGenerator.getInstance().getWorldManager().lockCurrentTime(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "mainInv-createItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                TommyGenerator.getInstance().getGuiManager().openWorldCreatorInventoryWorldNameInput(player);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInv-normalWorld"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                TommyGenerator.getInstance().getWorldManager().createNewWorld(player, event.getClickedInventory().getItem(9).getItemMeta().getDisplayName(), World.Environment.NORMAL);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInv-netherWorld"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                TommyGenerator.getInstance().getWorldManager().createNewWorld(player, event.getClickedInventory().getItem(9).getItemMeta().getDisplayName(), World.Environment.NETHER);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInv-endWorld"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                TommyGenerator.getInstance().getWorldManager().createNewWorld(player, event.getClickedInventory().getItem(9).getItemMeta().getDisplayName(), World.Environment.THE_END);
            }
        }

        if(event.getInventory() != null && event.getInventory().getType().equals(InventoryType.ANVIL)) {
            var aSlot1 = event.getInventory().getItem(0);
            var aSlot2 = event.getInventory().getItem(1);
            var aSlot3 = event.getInventory().getItem(2);
            if (aSlot1 != null || aSlot2 != null || aSlot3 != null) {
                if(aSlot1.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInvWorldNameInput-paperItem"), PersistentDataType.DOUBLE)
                    || aSlot2.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInvWorldNameInput-paperItem"), PersistentDataType.DOUBLE)
                    || aSlot3.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInvWorldNameInput-paperItem"), PersistentDataType.DOUBLE)) {
                    event.setCancelled(true);
                    if(event.getSlot() == 2 && event.getCurrentItem() != null && event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInvWorldNameInput-paperItem"), PersistentDataType.DOUBLE)) {
                        var itemR = event.getCurrentItem();
                        event.getClickedInventory().clear();
                        TommyGenerator.getInstance().getGuiManager().openWorldCreatorInventory((Player) event.getWhoClicked(), itemR.getItemMeta().getDisplayName());
                    }
                }
            }


        }


        if(inventoryView.title().equals(Component.text("§2TommyGenerator v" + TommyGenerator.getInstance().getPluginVersion()))) {
            event.setCancelled(true);
            if(event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                if(event.getCurrentItem().getItemMeta().displayName() != null) {
                    var persistentContainer = event.getCurrentItem().getItemMeta().getPersistentDataContainer();
                    if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "mainInv-worldsItem"), PersistentDataType.DOUBLE)) {
                        TommyGenerator.getInstance().getGuiManager().openWorldListInventory((Player) event.getWhoClicked());
                    }
                    if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "mainMenuItem"), PersistentDataType.DOUBLE)) {
                        TommyGenerator.getInstance().getGuiManager().openMainInventory((Player) event.getWhoClicked());
                    }
                }

            }
        }
        if(inventoryView.title().equals(Component.text("§2§lWorlds"))) {
            event.setCancelled(true);
            if(event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                if(event.getCurrentItem().getItemMeta().displayName() != null) {
                    var persistentContainer = event.getCurrentItem().getItemMeta().getPersistentDataContainer();
                    if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "mainMenuItem"), PersistentDataType.DOUBLE)) {
                        TommyGenerator.getInstance().getGuiManager().openMainInventory((Player) event.getWhoClicked());
                    }
                    if(!event.getCurrentItem().getType().isAir() && !event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE) && !event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, event.getCurrentItem().getItemMeta().getDisplayName(), Bukkit.getWorld(event.getCurrentItem().getItemMeta().getDisplayName().substring(2)));
                    }
                }

            }
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        //event.message(Component.text(event.getPlayer().getName() + " §8-> §a").append(event.message()));
        event.renderer((source, sourceDisplayName, message, viewer) -> sourceDisplayName
                .append(Component
                        .text(" > "))
                .append(message)
        );
    }
}
