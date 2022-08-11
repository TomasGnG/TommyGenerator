package com.tomasgng.utils;

import com.tomasgng.TommyGenerator;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static net.kyori.adventure.text.format.NamedTextColor.DARK_GREEN;

public class GUIManager {

    ItemStack glass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setKey("tg-defaultGlassItem").setDisplayName(" ").build();
    ItemStack mainMenuItem = new SkullBuilder().setKey("mainMenuItem").setDisplayName("§7Back to main menu").setLore("§8§o→ Click to open the main menu").setPlayerProfileByURL("https://textures.minecraft.net/texture/f6dab7271f4ff04d5440219067a109b5c0c1d1e01ec602c0020476f7eb612180").build();
    WorldManager worldManager;

    public GUIManager() {
        worldManager = TommyGenerator.getInstance().getWorldManager();
    }

    public void openMainInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 3*9, Component.text("§2TommyGenerator v" + TommyGenerator.getInstance().getPluginVersion()));

        var worldsItem = new ItemBuilder(Material.GRASS_BLOCK).setKey("mainInv-worldsItem").setDisplayName("§aWorlds").setLore("§8§o→ Click to open the worlds list").build();
        var createItem = new ItemBuilder(Material.PAPER).setKey("mainInv-createItem").setDisplayName("§2Create world").setLore("§8§o→ Click to generate a new world").build();
        var portalItem = new ItemBuilder(Material.ENDER_EYE).setKey("mainInv-portalItem").setDisplayName("§2Create portal").setLore("§8§o→ Click to create a portal between two worlds", "§8§o→ §7Active: §f" + TommyGenerator.getInstance().getPortalManager().getActivePortalCount()).build();

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }
        inventory.setItem(11, worldsItem);
        inventory.setItem(15, createItem);
        inventory.setItem(22, portalItem);

        player.openInventory(inventory);
    }

    public void openWorldListInventory(Player player) {
        var worldList = Bukkit.getWorlds();
        Inventory inventory;
        inventory = Bukkit.createInventory(null, 6*9, Component.text("§2§lWorlds"));

        for (int i = 0; i < worldList.size(); i++) {
            var playerCount = worldList.get(i).getPlayerCount();
            var environment = worldList.get(i).getEnvironment().name();
            String weather;
            if(worldList.get(i).isClearWeather()) {
                weather = "§aClear";
            } else {
                weather = "§cStorm/Raining";
            }
            String time;
            if(worldList.get(i).isDayTime()){
                time = "§aDay";
            } else {
                time = "§cNight";
            }
            switch (worldList.get(i).getEnvironment()) {
                case NORMAL ->
                        inventory.setItem(i, new ItemBuilder(Material.GRASS_BLOCK).setLore("§8→ §7Players: §a" + playerCount, "§8→ §7Environment: §a" + environment, "§8→ §7GameMode: §f" + TommyGenerator.getInstance().getWorldManager().getGameMode(worldList.get(i)), "§8→ §7Entry: §f" + TommyGenerator.getInstance().getWorldManager().getEntryMode(worldList.get(i)), "§8→ §7Time: " + time, "§8→ §7Weather: " + weather).setDisplayName("§a" + worldList.get(i).getName()).build());
                case NETHER ->
                        inventory.setItem(i, new ItemBuilder(Material.NETHERRACK).setLore("§8→ §7Players: §a" + playerCount, "§8→ §7Environment: §c" + environment, "§8→ §7GameMode: §f" + TommyGenerator.getInstance().getWorldManager().getGameMode(worldList.get(i)), "§8→ §7Entry: §f" + TommyGenerator.getInstance().getWorldManager().getEntryMode(worldList.get(i))).setDisplayName("§c" + worldList.get(i).getName()).build());
                case THE_END ->
                        inventory.setItem(i, new ItemBuilder(Material.END_STONE).setLore("§8→ §7Players: §a" + playerCount, "§8→ §7Environment: §f" + environment, "§8→ §7GameMode: §f" + TommyGenerator.getInstance().getWorldManager().getGameMode(worldList.get(i)), "§8→ §7Entry: §f" + TommyGenerator.getInstance().getWorldManager().getEntryMode(worldList.get(i))).setDisplayName("§f" + worldList.get(i).getName()).build());
                case CUSTOM ->
                        inventory.setItem(i, new ItemBuilder(Material.BARRIER).setLore("§8→ §7Players: §a" + playerCount, "§8→ §7Environment: §7" + environment, "§8→ §7GameMode: §f" + TommyGenerator.getInstance().getWorldManager().getGameMode(worldList.get(i)), "§8→ §7Entry: §f" + TommyGenerator.getInstance().getWorldManager().getEntryMode(worldList.get(i)), "§8→ §7Time: " + time, "§8→ §7Weather: " + weather).setDisplayName("§7" + worldList.get(i).getName()).build());
            }
        }

        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }
        inventory.setItem(inventory.getSize()-1, mainMenuItem);
        player.openInventory(inventory);
    }

    public void openWorldEditInventory(Player player, String displayName, World world) {
        Inventory inventory = Bukkit.createInventory(null, 6*9, Component.text(displayName));

        var tpItem = new ItemBuilder(Material.ENDER_PEARL).setKey("worldEditInv-tpItem").setDisplayName("§f「 §aTeleport §f」").setLore("§8§o→ Teleport to the world spawn").build();
        var lockTimeItem = new ItemBuilder(Material.DAYLIGHT_DETECTOR).setKey("worldEditInv-lockTimeItem").setDisplayName("§f「 §aDaylight Cycle §f」").setLore("§8§o→ Switches the daylight cycle mode", "§8→ §f" + world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)).build();
        var lockWeatherItem = new ItemBuilder(Material.WATER_BUCKET).setKey("worldEditInv-lockWeatherItem").setDisplayName("§f「 §aWeather Cycle §f」").setLore("§8§o→ Switches the weather cycle mode", "§8→ §f" + world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE)).build();
        var setWorldSpawnItem = new ItemBuilder(Material.WHITE_BED).setKey("worldEditInv-setWorldSpawnItem").setDisplayName("§f「 §aSet World Spawn §f」").setLore("§8§o→ Sets the world spawn to your current location").build();
        var setAnimalSpawningItem = new SkullBuilder().setKey("worldEditInv-setAnimalSpawningItem").setDisplayName("§f「 §aAnimal Spawning §f」").setLore("§8§o→ Toggles the spawning of animals like cows etc.", "§8→ §f" + TommyGenerator.getInstance().getWorldManager().animalSpawning(world)).setPlayerProfileByURL("https://textures.minecraft.net/texture/c5a9cd58d4c67bccc8fb1f5f756a2d381c9ffac2924b7f4cb71aa9fa13fb5c").build();
        var setMonsterSpawningItem = new ItemBuilder(Material.CREEPER_HEAD).setKey("worldEditInv-setMonsterSpawningItem").setDisplayName("§f「 §aMonster Spawning §f」").setLore("§8§o→ Toggles the spawning of monsters like zombies etc.", "§8→ §f" + TommyGenerator.getInstance().getWorldManager().monsterSpawning(world)).build();
        var togglePvPItem = new ItemBuilder(Material.IRON_SWORD).setKey("worldEditInv-togglePvPItem").setDisplayName("§f「 §aPvP §f」").setLore("§8§o→ Toggles the hitting of other players", "§8→ §f" + TommyGenerator.getInstance().getWorldManager().allowedPvP(world)).build();
        var toggleDifficulty = new ItemBuilder(Material.WOODEN_SWORD).setKey("worldEditInv-toggleDifficulty").setDisplayName("§f「 §aDifficulty §f」").setLore("§8§o→ Toggles the difficulty of the world", "§8→ §f" + TommyGenerator.getInstance().getWorldManager().getDifficulty(world)).build();
        var deleteWorld = new ItemBuilder(Material.BARRIER).setKey("worldEditInv-deleteWorld").setDisplayName("§f「 ❌ §cDelete §f」").setLore("§8§o→ §c§oThis will delete the world §4§limmediately§c§o!").build();
        var renameWorld = new ItemBuilder(Material.PAPER).setKey("worldEditInv-renameWorld").setDisplayName("§f「 ✎ §cRename §f」").setLore("§8§o→ §c§oThis will rename the world!", "§8§o→ §c§oThe old directory will be deleted with a 20 sec delay to avoid problems!").build();
        var toggleGameMode = new ItemBuilder(Material.GOLDEN_PICKAXE).setKey("worldEditInv-toggleGameMode").setDisplayName("§f「 §aGameMode §f」").setLore("§8§o→ Toggles the gamemode of the world.", "§8§o→ Players gamemode will change to the world gamemode.", "§8§o→ §f" + TommyGenerator.getInstance().getWorldManager().getGameMode(world)).build();
        var toggleEntry = new ItemBuilder(Material.ENDER_EYE).setKey("worldEditInv-toggleEntry").setDisplayName("§f「 ✓ §cEntry §f」").setLore("§8§o→ Toggles the entry mode for players without the permission", "§8§o→ Permission will be like this: tommygenerator.entry." + world.getName().toLowerCase(), "§8§o→ §f" + TommyGenerator.getInstance().getWorldManager().getEntryMode(world)).build();
        var editEffectsLore = new ArrayList<String>();
        editEffectsLore.add("§8§o→ Sets the effects for the world");
        editEffectsLore.add("§8§o→ Players will get the activated effects permanently when entering");
        editEffectsLore.add("§8§o→ Active:");
        for (int i = 0; i < worldManager.getActiveEffects(world).size(); i++) {
            editEffectsLore.add("§8§o→ §f" + worldManager.getActiveEffects(world).get(i));
        }
        var editEffects = new ItemBuilder(Material.GLASS_BOTTLE).setKey("worldEditInv-editEffects").setDisplayName("§f「 §cEffects §f」").setLore(editEffectsLore.toArray(String[]::new)).build();
        var duplicateWorld = new ItemBuilder(Material.GLOW_ITEM_FRAME).setKey("worldEditInv-duplicateWorld").setDisplayName("§f「 ✎ §cDuplicate §f」").setLore("§8§o→ Duplicate the world with a different name!").build();
        var backupItem = new ItemBuilder(Material.BOOKSHELF).setKey("worldEditInv-backupItem").setDisplayName("§f「 §2Backup §f」").setLore("§8§o→ Create a backup from this world", "§8§o→ The backup will be saved in the worlds folder as WorldName-Backup.zip!").build();

        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }

        inventory.setItem(inventory.getSize()-1, mainMenuItem);
        inventory.setItem(inventory.getSize()-9, deleteWorld);
        inventory.setItem(inventory.getSize()-8, renameWorld);
        inventory.setItem(inventory.getSize()-7, toggleEntry);
        inventory.setItem(inventory.getSize()-6, duplicateWorld);
        inventory.setItem(inventory.getSize()-5, backupItem);
        inventory.setItem(0, tpItem);
        inventory.setItem(1, lockTimeItem);
        inventory.setItem(2, lockWeatherItem);
        inventory.setItem(3, setWorldSpawnItem);
        inventory.setItem(4, setAnimalSpawningItem);
        inventory.setItem(5, setMonsterSpawningItem);
        inventory.setItem(6, togglePvPItem);
        inventory.setItem(7, toggleDifficulty);
        inventory.setItem(8, toggleGameMode);
        inventory.setItem(9, editEffects);

        player.openInventory(inventory);
    }

    public void openWorldEditEffectsInventory(Player player, String displayName, World world) {
        var inventory = Bukkit.createInventory(null, 6*9, displayName);
        var activeEffects = worldManager.getActiveEffects(world);

        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }

        for (int i = 0; i < PotionEffectType.values().length; i++) {
            if(activeEffects.contains(PotionEffectType.values()[i].getName())) {
                inventory.setItem(i, new ItemBuilder(Material.EXPERIENCE_BOTTLE).setKey("worldEditEffectsInv-effectType").setDisplayName("§a" + PotionEffectType.values()[i].getName()).build());
            } else {
                inventory.setItem(i, new ItemBuilder(Material.GLASS_BOTTLE).setKey("worldEditEffectsInv-effectType").setDisplayName("§c" + PotionEffectType.values()[i].getName()).build());
            }
        }

        inventory.setItem(inventory.getSize()-1, mainMenuItem);

        player.openInventory(inventory);
    }

    public void openWorldCreatorInventory(Player player, String world) {
        var inventory = Bukkit.createInventory(null, 2*9, Component.text("Choose your world type..", DARK_GREEN));

        var normalWorld = new ItemBuilder(Material.GRASS_BLOCK).setKey("worldCreatorInv-normalWorld").setDisplayName("§aNormal").build();
        var netherWorld = new ItemBuilder(Material.NETHERRACK).setKey("worldCreatorInv-netherWorld").setDisplayName("§cNether").build();
        var endWorld = new ItemBuilder(Material.END_STONE).setKey("worldCreatorInv-endWorld").setDisplayName("§fEnd").build();

        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }

        inventory.setItem(inventory.getSize()-1, mainMenuItem);
        inventory.setItem(0, normalWorld);
        inventory.setItem(1, netherWorld);
        inventory.setItem(2, endWorld);
        inventory.setItem(9, new ItemBuilder(Material.PAPER).setKey("tg-defaultGlassItem").setDisplayName(world).build());

        player.openInventory(inventory);
    }

    public void openWorldCreatorInventoryWorldNameInput(Player player) {
        player.openAnvil(player.getLocation(), true);
        player.getOpenInventory().setItem(0, new ItemBuilder(Material.PAPER).setKey("worldCreatorInvWorldNameInput-paperItem").setDisplayName("Your world name").build());
    }

    public void openWorldEditInventoryRenameWorldInput(Player player, World world) {
        player.openAnvil(player.getLocation(), true);
        player.getOpenInventory().setItem(0, new ItemBuilder(Material.PAPER).setKey("worldEditInvRenameWorldInput-paperItem").setKey(world.getUID().toString()).setDisplayName("Your new world name").build());
    }

    public void openWorldEditInventoryDuplicateWorldInput(Player player, World world) {
        player.openAnvil(player.getLocation(), true);
        player.getOpenInventory().setItem(0, new ItemBuilder(Material.PAPER).setKey("worldEditInvDuplicateWorldInput-paperItem").setKey(world.getUID().toString()).setDisplayName("Duplicate world name").build());
    }

    public void openPortalChooseWorldInventory(Player player) {
        var worldList = Bukkit.getWorlds();
        Inventory inventory;
        inventory = Bukkit.createInventory(null, 6*9, Component.text("§2Choose destination world"));

        for (int i = 0; i < worldList.size(); i++) {
            switch (worldList.get(i).getEnvironment()) {
                case NORMAL ->
                        inventory.setItem(i, new ItemBuilder(Material.GRASS_BLOCK).setDisplayName("§a" + worldList.get(i).getName()).setKey("portalChooseWorldInv-World").build());
                case NETHER ->
                        inventory.setItem(i, new ItemBuilder(Material.NETHERRACK).setDisplayName("§c" + worldList.get(i).getName()).setKey("portalChooseWorldInv-World").build());
                case THE_END ->
                        inventory.setItem(i, new ItemBuilder(Material.END_STONE).setDisplayName("§f" + worldList.get(i).getName()).setKey("portalChooseWorldInv-World").build());
                case CUSTOM ->
                        inventory.setItem(i, new ItemBuilder(Material.BARRIER).setDisplayName("§7" + worldList.get(i).getName()).setKey("portalChooseWorldInv-World").build());
            }
        }

        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }
        inventory.setItem(inventory.getSize()-1, mainMenuItem);
        player.openInventory(inventory);
    }
}
