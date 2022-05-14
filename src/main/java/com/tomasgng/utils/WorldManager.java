package com.tomasgng.utils;

import com.tomasgng.TommyGenerator;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldManager {

    public File configFile = new File("plugins/TommyGenerator/worlds.yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);

    public WorldManager() {
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
                cfg.set("Worlds", new ArrayList<String>());
                var comments = new ArrayList<String>();
                comments.add("#DO NOT CHANGE ANYTHING HERE!!!");
                cfg.setComments("Worlds", comments);
                save();
            } catch (IOException e) {}
        }
        loadCreatedWorlds();
    }

    // Worlds file configuration

    public void reload() {
        cfg = YamlConfiguration.loadConfiguration(configFile);
    }

    public void save() {
        try {
            cfg.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        reload();
    }

    private void loadCreatedWorlds() {
        if(!cfg.isSet("Worlds"))
            return;
        var list = cfg.getConfigurationSection("Worlds").getKeys(true).stream().toList();
        WorldCreator worldCreator;
        for (int i = 0; i < list.size(); i++) {
            worldCreator = new WorldCreator(list.get(i));
            worldCreator.environment(World.Environment.valueOf(cfg.getString("Worlds." + list.get(i))));
            worldCreator.createWorld();
        }
    }

    private void addWorldToList(String world) {
        reload();
        List<String> worlds = cfg.getStringList("Worlds");
        if(!worlds.contains(world)) {
            worlds.add(world);
            for (int i = 0; i < worlds.size(); i++) {
                cfg.set("Worlds." + worlds.get(i), Bukkit.getWorld(worlds.get(i)).getEnvironment().name());
            }

            save();
            //player.sendMessage("§aThe world \" " + world + "\" has been successfully created");
            return;
        }
    }

    private void removeWorldFromList(String world) {
        reload();
        List<String> worlds = cfg.getStringList("Worlds");
        if(worlds.contains(world)) {
            worlds.remove(world);
            cfg.set("Worlds." + world, null);
            for (int i = 0; i < worlds.size(); i++) {
                cfg.set("Worlds." + worlds.get(i), Bukkit.getWorld(worlds.get(i)).getEnvironment().name());
            }
            save();
            return;
        }
    }

    // World Creator

    public void createNewWorld(Player player, String worldName, World.Environment environment) {
        player.sendMessage("§aCreating world..");
        WorldCreator creator = null;
        try {
            creator = new WorldCreator(worldName);
        } catch (IllegalArgumentException exception) {
            player.sendMessage("§cFailed!\n§cError: " + exception.getMessage());
            TommyGenerator.getInstance().getGuiManager().openMainInventory(player);
            return;
        }
        creator.environment(environment);
        creator.createWorld();
        addWorldToList(creator.name());
        player.sendMessage("§aCreating world.. Success!");
        TommyGenerator.getInstance().getGuiManager().openWorldListInventory(player);
    }

    // World settings

    public void teleportToWorldSpawn(Player player, World world) {
        player.teleportAsync(world.getSpawnLocation());
        player.sendMessage("§aTeleported to the world §a§l" + world.getName() + "§a!");
    }

    public void lockCurrentTime(Player player, World world) {
        if(world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE).booleanValue()) {
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
            return;
        }
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public void lockCurrentWeather(Player player, World world) {
        if(world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE).booleanValue()) {
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
            return;
        }
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public void setWorldSpawn(Player player, World world) {
        world.setSpawnLocation(player.getLocation());
        player.sendMessage("§aWorld spawn was set to your location!");
    }

    public void toggleAnimalSpawning(Player player, World world) {
        var currentSpawnLimit = world.getSpawnLimit(SpawnCategory.ANIMAL);
        if(currentSpawnLimit != 0) {
            world.setSpawnLimit(SpawnCategory.ANIMAL, 0);
            player.sendMessage("§aAnimals won't spawn naturally.");
            TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
            return;
        }
        world.setSpawnLimit(SpawnCategory.ANIMAL, 10);
        player.sendMessage("§aAnimals will spawn naturally.");
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public boolean animalSpawning(World world) {
        if(world.getSpawnLimit(SpawnCategory.ANIMAL) == 0) {
            return false;
        }
        return true;
    }

    public void toggleMonsterSpawning(Player player, World world) {
        var currentSpawnLimit = world.getSpawnLimit(SpawnCategory.MONSTER);
        if(currentSpawnLimit != 0) {
            world.setSpawnLimit(SpawnCategory.MONSTER, 0);
            player.sendMessage("§aMonsters won't spawn naturally.");
            TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
            return;
        }
        world.setSpawnLimit(SpawnCategory.MONSTER, 70);
        player.sendMessage("§aMonsters will spawn naturally.");
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public boolean monsterSpawning(World world) {
        if(world.getSpawnLimit(SpawnCategory.MONSTER) == 0) {
            return false;
        }
        return true;
    }

    public void togglePvP(Player player, World world) {
        if(allowedPvP(world)) {
            world.setPVP(false);
            player.sendMessage("§aPvP is now deactivated!");
            TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
            return;
        }
        world.setPVP(true);
        player.sendMessage("§aPvP is now activated!");
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public boolean allowedPvP(World world) {
        return world.getPVP();
    }

    public void toggleDifficulty(Player player, World world) {
        switch (world.getDifficulty()) {
            case PEACEFUL:
                world.setDifficulty(Difficulty.EASY);
                player.sendMessage("§aNew difficulty: §2" + Difficulty.EASY.name());
                break;
            case EASY:
                world.setDifficulty(Difficulty.NORMAL);
                player.sendMessage("§aNew difficulty: §2" + Difficulty.NORMAL.name());
                break;
            case NORMAL:
                world.setDifficulty(Difficulty.HARD);
                player.sendMessage("§aNew difficulty: §2" + Difficulty.HARD.name());
                break;
            case HARD:
                world.setDifficulty(Difficulty.PEACEFUL);
                player.sendMessage("§aNew difficulty: §2" + Difficulty.PEACEFUL.name());
                break;
        }
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public String getDifficulty(World world) {
        return world.getDifficulty().name();
    }

    public void deleteWorld(Player player, World world) {
        Bukkit.unloadWorld(world, false);
        if(!delDirectory(world.getWorldFolder())) {
            player.sendMessage("§aWorld §2" + world.getName() + " §awas successfully deleted.");
            removeWorldFromList(world.getName());
            TommyGenerator.getInstance().getGuiManager().openWorldListInventory(player);
            return;
        }
        player.sendMessage("§cWorld §4" + world.getName() + " §ccouldn't be deleted.");
        TommyGenerator.getInstance().getGuiManager().openWorldListInventory(player);
    }

    private boolean delDirectory(File folder) {
        File[] files = folder.listFiles();
        if(files != null) {
            for (File f : files) {
                if(f.isDirectory()) {
                    delDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
        return folder.exists();
    }
}
