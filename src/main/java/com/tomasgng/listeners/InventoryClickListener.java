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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.UUID;

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
            if(containsKey("mainMenuItem", persistentContainer)) {
                event.setCancelled(true);
                guiManager.openMainInventory(player);
            }
            if(containsKey("worldEditInv-tpItem", persistentContainer)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.teleportToWorldSpawn(player, world);
            }
            if(containsKey("tg-defaultGlassItem", persistentContainer)) {
                event.setCancelled(true);
            }
            if(containsKey("worldEditInv-lockTimeItem", persistentContainer)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.lockCurrentTime(player, world);
            }
            if(containsKey("mainInv-createItem", persistentContainer)) {
                event.setCancelled(true);
                guiManager.openWorldCreatorInventoryWorldNameInput(player);
            }
            if(containsKey("worldCreatorInv-normalWorld", persistentContainer)) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> worldManager.createNewWorld(player, Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(9)).getItemMeta().getDisplayName(), World.Environment.NORMAL));
            }
            if(containsKey("worldCreatorInv-netherWorld", persistentContainer)) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> worldManager.createNewWorld(player, Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(9)).getItemMeta().getDisplayName(), World.Environment.NETHER));
            }
            if(containsKey("worldCreatorInv-endWorld", persistentContainer)) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> worldManager.createNewWorld(player, Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(9)).getItemMeta().getDisplayName(), World.Environment.THE_END));
            }
            if(containsKey("worldEditInv-lockWeatherItem", persistentContainer)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.lockCurrentWeather(player, world);
            }
            if(containsKey("worldEditInv-setWorldSpawnItem", persistentContainer)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.setWorldSpawn(player, world);
            }
            if(containsKey("worldEditInv-setAnimalSpawningItem", persistentContainer)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.toggleAnimalSpawning(player, world);
            }
            if(containsKey("worldEditInv-setMonsterSpawningItem", persistentContainer)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.toggleMonsterSpawning(player, world);
            }
            if(containsKey("worldEditInv-togglePvPItem", persistentContainer)) {
                event.setCancelled(true);
                worldManager.togglePvP(player, world);
            }
            if(containsKey("worldEditInv-toggleDifficulty", persistentContainer)) {
                event.setCancelled(true);
                assert world != null;
                worldManager.toggleDifficulty(player, world);
            }
            if(containsKey("worldEditInv-deleteWorld", persistentContainer)) {
                event.setCancelled(true);
                worldManager.deleteWorld(player, Bukkit.getWorld(event.getView().getTitle().substring(2)));
            }
            if(containsKey("worldEditInv-renameWorld", persistentContainer)) {
                event.setCancelled(true);
                guiManager.openWorldEditInventoryRenameWorldInput(player, Objects.requireNonNull(Bukkit.getWorld(event.getView().getTitle().substring(2))));
            }
            if(containsKey("worldEditInv-toggleGameMode", persistentContainer)) {
                event.setCancelled(true);
                worldManager.toggleGameMode(player, Bukkit.getWorld(event.getView().getTitle().substring(2)));
            }
            if(containsKey("worldEditInv-toggleEntry", persistentContainer)) {
                event.setCancelled(true);
                worldManager.toggleEntry(player, Bukkit.getWorld(event.getView().getTitle().substring(2)));
            }
            if(containsKey("worldEditInv-editEffects", persistentContainer)) {
                event.setCancelled(true);
                guiManager.openWorldEditEffectsInventory(player, event.getView().getTitle(), world);
            }
            if(containsKey("worldEditEffectsInv-effectType", persistentContainer)) {
                event.setCancelled(true);
                worldManager.toggleEffect(player, world, PotionEffectType.getByName(event.getCurrentItem().getItemMeta().getDisplayName().substring(2)));
                guiManager.openWorldEditEffectsInventory(player, event.getView().getTitle(), world);
            }
            if(containsKey("mainInv-portalItem", persistentContainer)) {
                event.setCancelled(true);
                guiManager.openPortalChooseWorldInventory(player);
            }
            if(containsKey("portalChooseWorldInv-World", persistentContainer)) {
                event.setCancelled(true);
                TommyGenerator.getInstance().getPortalManager().togglePlayersCreatingState(player, Bukkit.getWorld(event.getCurrentItem().getItemMeta().getDisplayName().substring(2)), false);
            }
            if(containsKey("worldEditInv-duplicateWorld", persistentContainer)) {
                event.setCancelled(true);
                guiManager.openWorldEditInventoryDuplicateWorldInput(player, Objects.requireNonNull(Bukkit.getWorld(event.getView().getTitle().substring(2))));
            }
            if(containsKey("worldEditInv-backupItem", persistentContainer)) {
                event.setCancelled(true);

                worldManager.backupWorld(player, Objects.requireNonNull(Bukkit.getWorld(event.getView().getTitle().substring(2))));
            }
        }

        if(event.getInventory().getType().equals(InventoryType.ANVIL)) {
            for (int i = 0; i < 3; i++) {
                if(event.getInventory().getItem(i) != null) {
                    var persistentContainer = event.getInventory().getItem(i).getItemMeta().getPersistentDataContainer();
                    if(containsKey("worldCreatorInvWorldNameInput-paperItem", persistentContainer)) {
                        event.setCancelled(true);
                        if(event.getSlot() == 2 && event.getCurrentItem() != null && containsKey("worldCreatorInvWorldNameInput-paperItem", persistentContainer)) {
                            var itemR = event.getCurrentItem();
                            Objects.requireNonNull(event.getClickedInventory()).clear();
                            guiManager.openWorldCreatorInventory((Player) event.getWhoClicked(), itemR.getItemMeta().getDisplayName());
                        }
                    }
                    if(containsKey("worldEditInvRenameWorldInput-paperItem", persistentContainer)) {
                        event.setCancelled(true);
                        if(event.getSlot() == 2 && event.getCurrentItem() != null && containsKey("worldEditInvRenameWorldInput-paperItem", event.getCurrentItem().getItemMeta().getPersistentDataContainer())) {
                            var itemR = event.getCurrentItem();
                            Objects.requireNonNull(event.getClickedInventory()).clear();
                            Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> {
                                for(var item : itemR.getItemMeta().getPersistentDataContainer().getKeys()) {
                                    if(!item.getKey().contains("worldedit")) {
                                        if(Bukkit.getWorld(item.getKey()) == null)
                                            return;
                                        worldManager.renameWorld(player, Bukkit.getWorld(item.getKey()), itemR.getItemMeta().getDisplayName(), Objects.requireNonNull(Bukkit.getWorld(item.getKey())).getEnvironment());
                                    }
                                }

                            });
                        }
                    }
                    if(containsKey("worldEditInvDuplicateWorldInput-paperItem", persistentContainer)) {
                        event.setCancelled(true);
                        if(event.getSlot() == 2 && event.getCurrentItem() != null && containsKey("worldEditInvDuplicateWorldInput-paperItem", event.getCurrentItem().getItemMeta().getPersistentDataContainer())) {
                            var itemR = event.getCurrentItem();
                            Objects.requireNonNull(event.getClickedInventory()).clear();
                            Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> {
                                for(var item : itemR.getItemMeta().getPersistentDataContainer().getKeys()) {
                                    if(!item.getKey().startsWith("worldedit")) {
                                        UUID uid = UUID.fromString(item.getKey());
                                        if(Bukkit.getWorld(uid) == null)
                                            return;
                                        worldManager.duplicateWorld(player, Bukkit.getWorld(uid), itemR.getItemMeta().getDisplayName());
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
                    if(containsKey("mainInv-worldsItem", persistentContainer)) {
                        guiManager.openWorldListInventory((Player) event.getWhoClicked());
                    }
                    if(containsKey("mainMenuItem", persistentContainer)) {
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
                    if(containsKey("mainMenuItem", persistentContainer)) {
                        guiManager.openMainInventory((Player) event.getWhoClicked());
                    }
                    if(!event.getCurrentItem().getType().isAir() && !event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE) && !event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                        guiManager.openWorldEditInventory(player, event.getCurrentItem().getItemMeta().getDisplayName(), Objects.requireNonNull(Bukkit.getWorld(event.getCurrentItem().getItemMeta().getDisplayName().substring(2))));
                    }
                }

            }
        }
    }

    private boolean containsKey(String key, PersistentDataContainer container) {
        return container.has(new NamespacedKey(TommyGenerator.getInstance(), key), PersistentDataType.DOUBLE);
    }
}
