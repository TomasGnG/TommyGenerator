package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import com.tomasgng.utils.GUIManager;
import com.tomasgng.utils.WorldManager;
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
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class InventoryClickListener implements Listener {

    public GUIManager guiManager = TommyGenerator.getInstance().getGuiManager();
    public WorldManager worldManager = TommyGenerator.getInstance().getWorldManager();
    
    @EventHandler
    public void on(InventoryClickEvent event) {
        var inventoryView = event.getView();
        var player = (Player) event.getWhoClicked();

        if(event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
            var persistentContainer = event.getCurrentItem().getItemMeta().getPersistentDataContainer();
            var world = Bukkit.getWorld(inventoryView.getTitle().substring(2));
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "mainMenuItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                guiManager.openMainInventory(player);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-tpItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.teleportToWorldSpawn(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "tg-defaultGlassItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-lockTimeItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.lockCurrentTime(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "mainInv-createItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                guiManager.openWorldCreatorInventoryWorldNameInput(player);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInv-normalWorld"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> worldManager.createNewWorld(player, Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(9)).getItemMeta().getDisplayName(), World.Environment.NORMAL));
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInv-netherWorld"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> worldManager.createNewWorld(player, Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(9)).getItemMeta().getDisplayName(), World.Environment.NETHER));
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInv-endWorld"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> worldManager.createNewWorld(player, Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(9)).getItemMeta().getDisplayName(), World.Environment.THE_END));
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-lockWeatherItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.lockCurrentWeather(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-setWorldSpawnItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.setWorldSpawn(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-setAnimalSpawningItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.toggleAnimalSpawning(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-setMonsterSpawningItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.toggleMonsterSpawning(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-togglePvPItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                worldManager.togglePvP(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-toggleDifficulty"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.toggleDifficulty(player, world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-deleteWorld"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                worldManager.deleteWorld(player, Bukkit.getWorld(event.getView().getTitle().substring(2)));
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-renameWorld"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                guiManager.openWorldEditInventoryRenameWorldInput(player, event.getView().getTitle().substring(2));
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-toggleGameMode"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                worldManager.toggleGameMode(player, Bukkit.getWorld(event.getView().getTitle().substring(2)));
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-toggleEntry"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                worldManager.toggleEntry(player, Bukkit.getWorld(event.getView().getTitle().substring(2)));
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInv-editEffects"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                guiManager.openWorldEditEffectsInventory(player, event.getView().getTitle(), world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditEffectsInv-effectType"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                worldManager.toggleEffect(player, world, PotionEffectType.getByName(event.getCurrentItem().getItemMeta().getDisplayName().substring(2)));
                guiManager.openWorldEditEffectsInventory(player, event.getView().getTitle(), world);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "mainInv-portalItem"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                guiManager.openPortalChooseWorldInventory(player);
            }
            if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "portalChooseWorldInv-World"), PersistentDataType.DOUBLE)) {
                event.setCancelled(true);
                TommyGenerator.getInstance().getPortalManager().togglePlayersCreatingState(player, Bukkit.getWorld(event.getCurrentItem().getItemMeta().getDisplayName().substring(2)), false);
            }
        }

        event.getInventory();
        if(event.getInventory().getType().equals(InventoryType.ANVIL)) {
            for (int i = 0; i < 3; i++) {
                if(event.getInventory().getItem(i) != null) {
                    if(event.getInventory().getItem(i) != null && Objects.requireNonNull(event.getInventory().getItem(i)).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInvWorldNameInput-paperItem"), PersistentDataType.DOUBLE)) {
                        event.setCancelled(true);
                        if(event.getSlot() == 2 && event.getCurrentItem() != null && event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldCreatorInvWorldNameInput-paperItem"), PersistentDataType.DOUBLE)) {
                            var itemR = event.getCurrentItem();
                            Objects.requireNonNull(event.getClickedInventory()).clear();
                            guiManager.openWorldCreatorInventory((Player) event.getWhoClicked(), itemR.getItemMeta().getDisplayName());
                        }
                    }
                    if(event.getInventory().getItem(i) != null && Objects.requireNonNull(event.getInventory().getItem(i)).getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInvRenameWorldInput-paperItem"), PersistentDataType.DOUBLE)) {
                        event.setCancelled(true);
                        if(event.getSlot() == 2 && event.getCurrentItem() != null && event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(TommyGenerator.getInstance(), "worldEditInvRenameWorldInput-paperItem"), PersistentDataType.DOUBLE)) {
                            var itemR = event.getCurrentItem();
                            Objects.requireNonNull(event.getClickedInventory()).clear();
                            Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> {
                                for(var item : itemR.getItemMeta().getPersistentDataContainer().getKeys()) {
                                    if(!item.getKey().contains("worldedit")) {
                                        if(Bukkit.getWorld(item.getKey()) == null)
                                            return;
                                        worldManager.renameWorld(player, Objects.requireNonNull(Bukkit.getWorld(item.getKey())).getName(), itemR.getItemMeta().getDisplayName(), Objects.requireNonNull(Bukkit.getWorld(item.getKey())).getEnvironment());
                                    }
                                }

                            });
                        }
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
                        guiManager.openWorldListInventory((Player) event.getWhoClicked());
                    }
                    if(persistentContainer.has(new NamespacedKey(TommyGenerator.getInstance(), "mainMenuItem"), PersistentDataType.DOUBLE)) {
                        guiManager.openMainInventory((Player) event.getWhoClicked());
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
                        guiManager.openMainInventory((Player) event.getWhoClicked());
                    }
                    if(!event.getCurrentItem().getType().isAir() && !event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE) && !event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                        guiManager.openWorldEditInventory(player, event.getCurrentItem().getItemMeta().getDisplayName(), Objects.requireNonNull(Bukkit.getWorld(event.getCurrentItem().getItemMeta().getDisplayName().substring(2))));
                    }
                }

            }
        }
    }
}
