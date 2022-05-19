package com.tomasgng.utils;

import com.tomasgng.TommyGenerator;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

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
            } catch (IOException ignored) {}
        }
        loadCreatedWorlds();
    }

    private void copyFolder(File source, File destination) {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }

            String[] files = source.list();

            assert files != null;
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);

                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = null;
            OutputStream out = null;

            try {
                in = new FileInputStream(source);
                out = new FileOutputStream(destination);

                byte[] buffer = new byte[1024];

                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } catch (Exception e) {
                try {
                    assert in != null;
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    assert out != null;
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
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
        reload();
        if(cfg.getConfigurationSection("Worlds") == null)
            return;
        var list = Objects.requireNonNull(cfg.getConfigurationSection("Worlds")).getKeys(true).stream().toList();
        WorldCreator worldCreator;
        for (String s : list) {
            worldCreator = new WorldCreator(s.split("\\.")[0]);
            try {
                worldCreator.environment(World.Environment.valueOf(cfg.getString("Worlds." + s.split("\\.")[0] + ".Environment")));
                worldCreator.createWorld();
            } catch (Exception e) {
                TommyGenerator.getInstance().getLogger().log(Level.SEVERE, "World \"" + s.split("\\.")[0] + "\" couldn't be loaded! Error: " + e.getMessage());
            }
        }
    }

    private void addWorldToList(String world) {
        reload();
        List<String> worlds = cfg.getStringList("Worlds");
        if(!worlds.contains(world)) {
            worlds.add(world);
            for (String s : worlds) {
                cfg.set("Worlds." + s + ".Environment", Objects.requireNonNull(Bukkit.getWorld(s)).getEnvironment().name());
                cfg.set("Worlds." + s + ".GameMode", "DISABLED");
            }
            save();
        }
    }

    private void removeWorldFromList(String world) {
        reload();
        List<String> worlds = cfg.getStringList("Worlds");
        worlds.remove(world);
        cfg.set("Worlds." + world, null);
        for (String s : worlds) {
            cfg.set("Worlds." + s, Objects.requireNonNull(Bukkit.getWorld(s)).getEnvironment().name());
        }
        save();
    }

    private void setWorldGameMode(World world, GameMode gameMode) {
        reload();
        if(gameMode == null) {
            cfg.set("Worlds." + world.getName() + ".GameMode", "DISABLED");
            save();
            return;
        }
        cfg.set("Worlds." + world.getName() + ".GameMode", gameMode.name());
        save();
    }

    // World Creator

    public void createNewWorld(Player player, String worldName, World.Environment environment) {
        player.sendMessage("§aCreating world..");
        WorldCreator creator;
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
        if(Boolean.TRUE.equals(world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE))) {
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
            return;
        }
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public void lockCurrentWeather(Player player, World world) {
        if(Boolean.TRUE.equals(world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE))) {
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
        return world.getSpawnLimit(SpawnCategory.ANIMAL) != 0;
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
        return world.getSpawnLimit(SpawnCategory.MONSTER) != 0;
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
            case PEACEFUL -> {
                world.setDifficulty(Difficulty.EASY);
                player.sendMessage("§aNew difficulty: §2" + Difficulty.EASY.name());
            }
            case EASY -> {
                world.setDifficulty(Difficulty.NORMAL);
                player.sendMessage("§aNew difficulty: §2" + Difficulty.NORMAL.name());
            }
            case NORMAL -> {
                world.setDifficulty(Difficulty.HARD);
                player.sendMessage("§aNew difficulty: §2" + Difficulty.HARD.name());
            }
            case HARD -> {
                world.setDifficulty(Difficulty.PEACEFUL);
                player.sendMessage("§aNew difficulty: §2" + Difficulty.PEACEFUL.name());
            }
        }
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public String getDifficulty(World world) {
        return world.getDifficulty().name();
    }

    public void toggleGameMode(Player player, World world) {
        switch (getGameMode(world)) {
            case "SURVIVAL" -> setWorldGameMode(world, GameMode.CREATIVE);
            case "CREATIVE" -> setWorldGameMode(world, GameMode.ADVENTURE);
            case "ADVENTURE" -> setWorldGameMode(world, GameMode.SPECTATOR);
            case "SPECTATOR" -> setWorldGameMode(world, null);
            case "DISABLED" -> setWorldGameMode(world, GameMode.SURVIVAL);
        }
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public String getGameMode(World world) {
        if(!cfg.isSet("Worlds." + world.getName() + ".GameMode")) {
            return "NOT SET";
        }
        return cfg.getString("Worlds." + world.getName() + ".GameMode");
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

    public void renameWorld(Player player, String oldWorldName, String newWorldName, World.Environment environment) {
        WorldCreator creator;
        try {
            creator = new WorldCreator(newWorldName);
        } catch (IllegalArgumentException exception) {
            player.sendMessage("§cFailed!\n§cError: " + exception.getMessage());
            TommyGenerator.getInstance().getGuiManager().openMainInventory(player);
            return;
        }

        player.sendMessage("§aUnloading the world §2" + oldWorldName + "§a...");
        if(Objects.requireNonNull(Bukkit.getWorld(oldWorldName)).getPlayerCount() != 0) {
            for(Player players : Objects.requireNonNull(Bukkit.getWorld(oldWorldName)).getPlayers()) {
                players.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
            }
        }
        Bukkit.unloadWorld(Objects.requireNonNull(Bukkit.getWorld(oldWorldName)), true);
        player.sendMessage("§aUnloaded world!");

        player.sendMessage("§aRenaming...");
        removeWorldFromList(oldWorldName);
        copyFolder(new File(oldWorldName), new File(newWorldName));
        player.sendMessage("§aRenaming was successful!");



        player.sendMessage("§aLoading the renamed world...");
        creator.environment(environment);
        var currentTimeMillis = System.currentTimeMillis();
        creator.createWorld();
        addWorldToList(newWorldName);
        player.sendMessage("§aLoading was successful!\n§aTime elapsed: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        player.sendMessage("§a§lRenaming was successful!");

        Bukkit.getScheduler().runTaskLater(TommyGenerator.getInstance(), () -> {
            if(delDirectory(new File(oldWorldName))) {
                player.sendMessage("§cFolder could not be deleted!");
            }
        }, 20*20);

        TommyGenerator.getInstance().getGuiManager().openWorldListInventory(player);
    }


}
