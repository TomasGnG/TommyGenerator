package com.tomasgng.utils;

import com.tomasgng.TommyGenerator;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIManager {

    ItemStack glass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setKey("tg-defaultGlassItem").setDisplayName(" ").build();
    ItemStack mainMenuItem = new SkullBuilder().setKey("mainMenuItem").setDisplayName("§7Back to main menu").setLore("§8§o→ Click to open the main menu").setPlayerProfileByURL("https://textures.minecraft.net/texture/f6dab7271f4ff04d5440219067a109b5c0c1d1e01ec602c0020476f7eb612180").build();

    public void openMainInventory(Player player) {
        var worldList = Bukkit.getWorlds();
        Inventory inventory = Bukkit.createInventory(null, 3*9, Component.text("§2TommyGenerator v" + TommyGenerator.getInstance().getPluginVersion()));

        var worldsItem = new ItemBuilder(Material.GRASS_BLOCK).setKey("mainInv-worldsItem").setDisplayName("§aWorlds").setLore("§8§o→ Click to open the worlds list").build();
        var createItem = new ItemBuilder(Material.PAPER).setKey("mainInv-createItem").setDisplayName("§2Create world").setLore("§8§o→ Click to generate a new world").build();

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }
        inventory.setItem(11, worldsItem);
        inventory.setItem(15, createItem);

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
                case NORMAL:
                    inventory.setItem(i, new ItemBuilder(Material.GRASS_BLOCK).setLore("§8→ §7Players: §a" + playerCount, "§8→ §7Environment: §a" + environment, "§8→ §7Time: " + time, "§8→ §7Weather: " + weather).setDisplayName("§a" + worldList.get(i).getName()).build());
                    break;
                case NETHER:
                    inventory.setItem(i, new ItemBuilder(Material.NETHERRACK).setLore("§8→ §7Players: §a" + playerCount, "§8→ §7Environment: §c" + environment).setDisplayName("§c" + worldList.get(i).getName()).build());
                    break;
                case THE_END:
                    inventory.setItem(i, new ItemBuilder(Material.END_STONE).setLore("§8→ §7Players: §a" + playerCount, "§8→ §7Environment: §f" + environment).setDisplayName("§f" + worldList.get(i).getName()).build());
                    break;
                case CUSTOM:
                    inventory.setItem(i, new ItemBuilder(Material.BARRIER).setLore("§8→ §7Players: §a" + playerCount, "§8→ §7Environment: §7" + environment, "§8→ §7Time: " + time, "§8→ §7Weather: " + weather).setDisplayName("§7" + worldList.get(i).getName()).build());
                    break;
            }
        }

        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }
        inventory.setItem(inventory.getSize()-1, mainMenuItem);
        player.openInventory(inventory);
    }

    public void openWorldEditInventory(Player player, String displayName, World world) {
        Inventory inventory = Bukkit.createInventory(null, 6*9, displayName);

        var tpItem = new ItemBuilder(Material.ENDER_PEARL).setKey("worldEditInv-tpItem").setDisplayName("§aTeleport to the world").setLore("§8§o→ Teleport to the world spawn").build();
        var lockTimeItem = new ItemBuilder(Material.DAYLIGHT_DETECTOR).setKey("worldEditInv-lockTimeItem").setDisplayName("§aDaylight cycle").setLore("§8§o→ Switches the daylight cycle mode", "§8→ §f" + world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE).booleanValue()).build();
        var lockWeatherItem = new ItemBuilder(Material.WATER_BUCKET).setKey("worldEditInv-lockWeatherItem").setDisplayName("§aWeather cycle").setLore("§8§o→ Switches the weather cycle mode", "§8→ §f" + world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE).booleanValue()).build();
        var setWorldSpawnItem = new ItemBuilder(Material.WHITE_BED).setKey("worldEditInv-setWorldSpawnItem").setDisplayName("§aSet world spawn").setLore("§8§o→ Sets the world spawn to your current location").build();
        var setAnimalSpawningItem = new SkullBuilder().setKey("worldEditInv-setAnimalSpawningItem").setDisplayName("§aAnimal spawning").setLore("§8§o→ Toggles the spawning of animals like cows etc.", "§8→ §f" + TommyGenerator.getInstance().getWorldManager().animalSpawning(world)).setPlayerProfileByURL("https://textures.minecraft.net/texture/c5a9cd58d4c67bccc8fb1f5f756a2d381c9ffac2924b7f4cb71aa9fa13fb5c").build();
        var setMonsterSpawningItem = new ItemBuilder(Material.CREEPER_HEAD).setKey("worldEditInv-setMonsterSpawningItem").setDisplayName("§aMonster spawning").setLore("§8§o→ Toggles the spawning of monsters like zombies etc.", "§8→ §f" + TommyGenerator.getInstance().getWorldManager().monsterSpawning(world)).build();
        var togglePvPItem = new ItemBuilder(Material.IRON_SWORD).setKey("worldEditInv-togglePvPItem").setDisplayName("§aPvP").setLore("§8§o→ Toggles the hitting of other players", "§8→ §f" + TommyGenerator.getInstance().getWorldManager().allowedPvP(world)).build();
        var toggleDifficulty = new ItemBuilder(Material.WOODEN_SWORD).setKey("worldEditInv-toggleDifficulty").setDisplayName("§aDifficulty").setLore("§8§o→ Toggles the difficulty of the world", "§8→ §f" + TommyGenerator.getInstance().getWorldManager().getDifficulty(world)).build();
        var deleteWorld = new ItemBuilder(Material.BARRIER).setKey("worldEditInv-deleteWorld").setDisplayName("§cDelete world").setLore("§8§o→ §c§oThis will delete the world without confirmation!").build();

        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }

        inventory.setItem(inventory.getSize()-1, mainMenuItem);
        inventory.setItem(inventory.getSize()-9, deleteWorld);
        inventory.setItem(0, tpItem);
        inventory.setItem(1, lockTimeItem);
        inventory.setItem(2, lockWeatherItem);
        inventory.setItem(3, setWorldSpawnItem);
        inventory.setItem(4, setAnimalSpawningItem);
        inventory.setItem(5, setMonsterSpawningItem);
        inventory.setItem(6, togglePvPItem);
        inventory.setItem(7, toggleDifficulty);

        player.openInventory(inventory);
    }

    public void openWorldCreatorInventory(Player player, String worldName) {
        var inventory = Bukkit.createInventory(null, 2*9, Component.text("§2Choose your world type.."));

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
        inventory.setItem(9, new ItemBuilder(Material.PAPER).setKey("tg-defaultGlassItem").setDisplayName(worldName).build());

        player.openInventory(inventory);
    }

    public void openWorldCreatorInventoryWorldNameInput(Player player) {
        HumanEntity humanEntity = player;
        humanEntity.openAnvil(humanEntity.getLocation(), true);

        humanEntity.getOpenInventory().setItem(0, new ItemBuilder(Material.PAPER).setKey("worldCreatorInvWorldNameInput-paperItem").setDisplayName("Your world name").build());

    }
}
