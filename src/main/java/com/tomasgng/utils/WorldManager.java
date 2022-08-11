package com.tomasgng.utils;

import com.tomasgng.TommyGenerator;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class WorldManager {

    public File configFile = new File("plugins/TommyGenerator/worlds.yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);
    public GUIManager guiManager;

    public WorldManager() {
        Bukkit.getScheduler().runTask(TommyGenerator.getInstance(), () -> guiManager = TommyGenerator.getInstance().getGuiManager());
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
                cfg.set("Worlds", new ArrayList<String>());
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

    private boolean duplicateFolder(Path source, Path destination) {
        var customFileVisitor = new CustomFileVisitor(source, destination);

        try {
            Files.walkFileTree(source, customFileVisitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new File(destination + "\\uid.dat").delete();
        return destination.toFile().exists();
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
            var worldName = cfg.getString("Worlds." + s.split("\\.")[0] + ".Name");
            var id = s.split("\\.")[0];
            if(worldName == null)  {
                continue;
            }
            worldCreator = new WorldCreator(worldName);
            if(!worldName.equalsIgnoreCase("world") && !worldName.equalsIgnoreCase("world_nether") && !worldName.equalsIgnoreCase("world_the_end")) {
                try {
                    worldCreator.environment(World.Environment.valueOf(cfg.getString("Worlds." + id + ".Environment")));
                    worldCreator.createWorld();
                } catch (Exception e) {
                    TommyGenerator.getInstance().getLogger().log(Level.SEVERE, "World \"" + worldName + "\" couldn't be loaded! Error: " + e.getMessage());
                }
            }
        }
    }

    private void addWorldToList(World world) {
        reload();
        var id = world.getUID();
        cfg.set("Worlds." + id + ".Name", world.getName());
        cfg.set("Worlds." + id + ".Environment", world.getEnvironment().name());
        cfg.set("Worlds." + id + ".GameMode", "DISABLED");
        cfg.set("Worlds." + id + ".Entry", EntryMode.NOT_SET.toString());
        save();
    }

    private void removeWorldFromList(World world) {
        reload();
        cfg.set("Worlds." + world.getUID(), null);
        save();
    }

    private void duplicateWorldFromList(World world, World duplicate) {
        reload();
        var id = world.getUID();
        var idDuplicate = duplicate.getUID();
        if(!cfg.isSet("Worlds." + idDuplicate)) {
            var path = "Worlds." + id;
            var name = duplicate.getName();
            var environment = getEnvironment(world);
            var gameMode = getGameMode(world);
            var entryMode = getEntryMode(world);
            cfg.set("Worlds." + idDuplicate + ".Name", name);
            cfg.set("Worlds." + idDuplicate + ".Environment", environment.name());
            cfg.set("Worlds." + idDuplicate + ".GameMode", gameMode);
            cfg.set("Worlds." + idDuplicate + ".Entry", entryMode.name());
        }
        save();
    }

    private void setWorldGameMode(World world, GameMode gameMode) {
        reload();
        if(gameMode == null) {
            cfg.set("Worlds." + world.getUID() + ".GameMode", "DISABLED");
            save();
            return;
        }
        cfg.set("Worlds." + world.getUID() + ".GameMode", gameMode.name());
        save();
    }

    private void setWorldEntryMode(World world, Boolean bool) {
        if(bool) {
            cfg.set("Worlds." + world.getUID() + ".Entry", "ALLOWED");
            save();
            return;
        }
        cfg.set("Worlds." + world.getUID() + ".Entry", "DENIED");
        save();
    }

    private void editEffectFromList(World world, PotionEffectType potionEffectType) {
        var path = "Worlds." + world.getUID() + ".Effects";
        List<String> effects;
        if(!cfg.isSet(path)) {
            effects = new ArrayList<>();
            effects.add(potionEffectType.getName());
            cfg.set(path, effects);
            save();
            return;
        }
        effects = cfg.getStringList("Worlds." + world.getUID() + ".Effects");
        if(effects.contains(potionEffectType.getName())) {
            effects.remove(potionEffectType.getName());
            cfg.set(path, effects);
            save();
            return;
        }
        effects.add(potionEffectType.getName());
        cfg.set(path, effects);
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
        addWorldToList(Objects.requireNonNull(Bukkit.getWorld(worldName)));
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
            default -> setWorldGameMode(world, GameMode.SURVIVAL);
        }
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public String getGameMode(World world) {
        if(!cfg.isSet("Worlds." + world.getUID() + ".GameMode")) {
            return "NOT SET";
        }
        return cfg.getString("Worlds." + world.getUID() + ".GameMode");
    }

    public void deleteWorld(Player player, World world) {
        Bukkit.unloadWorld(world, false);
        if(!delDirectory(world.getWorldFolder())) {
            player.sendMessage("§aWorld §2" + world.getName() + " §awas successfully deleted.");
            removeWorldFromList(world);
            TommyGenerator.getInstance().getGuiManager().openWorldListInventory(player);
            return;
        }
        player.sendMessage("§cWorld §4" + world.getUID() + " §ccouldn't be deleted.");
        TommyGenerator.getInstance().getGuiManager().openWorldListInventory(player);
    }

    public void renameWorld(Player player, World oldWorld, String newWorldName, World.Environment environment) {
        WorldCreator creator;
        try {
            creator = new WorldCreator(newWorldName);
        } catch (IllegalArgumentException exception) {
            player.sendMessage("§cFailed!\n§cError: " + exception.getMessage());
            TommyGenerator.getInstance().getGuiManager().openMainInventory(player);
            return;
        }

        player.sendMessage("§aUnloading the world §2" + oldWorld.getName() + "§a...");
        if(oldWorld.getPlayerCount() != 0) {
            for(Player players : oldWorld.getPlayers()) {
                players.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation());
            }
        }
        Bukkit.unloadWorld(oldWorld, true);
        player.sendMessage("§aUnloaded world!");

        player.sendMessage("§aRenaming...");
        removeWorldFromList(oldWorld);
        copyFolder(oldWorld.getWorldFolder(), new File(newWorldName));
        player.sendMessage("§aRenaming was successful!");



        player.sendMessage("§aLoading the renamed world...");
        creator.environment(environment);
        var currentTimeMillis = System.currentTimeMillis();
        creator.createWorld();
        addWorldToList(Objects.requireNonNull(Bukkit.getWorld(newWorldName)));
        player.sendMessage("§aLoading was successful!\n§aTime elapsed: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        player.sendMessage("§a§lRenaming was successful!");

        Bukkit.getScheduler().runTaskLater(TommyGenerator.getInstance(), () -> {
            if(delDirectory(oldWorld.getWorldFolder())) {
                player.sendMessage("§cFolder could not be deleted!");
            }
        }, 20*20);

        TommyGenerator.getInstance().getGuiManager().openWorldListInventory(player);
    }

    public void toggleEntry(Player player, World world) {
        switch (getEntryMode(world)) {
            case NOT_SET, DENIED -> setWorldEntryMode(world, true);
            case ALLOWED -> setWorldEntryMode(world, false);
        }
        TommyGenerator.getInstance().getGuiManager().openWorldEditInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public EntryMode getEntryMode(World world) {
        if(!cfg.isSet("Worlds." + world.getUID() + ".Entry")) {
            return EntryMode.NOT_SET;
        }
        return EntryMode.valueOf(cfg.getString("Worlds." + world.getUID() + ".Entry"));
    }

    public void toggleEffect(Player player, World world, PotionEffectType potionEffectType) {
        editEffectFromList(world, potionEffectType);
        guiManager.openWorldEditEffectsInventory(player, player.getOpenInventory().getTitle(), world);
    }

    public List<String> getActiveEffects(World world) {
        return cfg.getStringList("Worlds." + world.getUID() + ".Effects");
    }

    public void duplicateWorld(Player player, World world, String duplicateWorldName) {
        WorldCreator creator;
        try {
            creator = new WorldCreator(duplicateWorldName);
        } catch (IllegalArgumentException exception) {
            player.sendMessage("§cFailed!\n§cError: " + exception.getMessage());
            TommyGenerator.getInstance().getGuiManager().openMainInventory(player);
            return;
        }
        if(duplicateFolder(Paths.get(world.getName()), Paths.get(duplicateWorldName))) {
            creator.environment(getEnvironment(world));
            player.sendMessage("§aDuplicating world...");
            creator.createWorld();
            player.sendMessage("§aWorld §2" + world.getName() + " §awas duplicated with the name §2" + duplicateWorldName);
            duplicateWorldFromList(world, Objects.requireNonNull(Bukkit.getWorld(duplicateWorldName)));
            guiManager.openWorldListInventory(player);
            return;
        }
        player.sendMessage("§cWorld §c§l" + world.getName() + " §ccouldn't be duplicated!");
        guiManager.openWorldListInventory(player);
    }

    public World.Environment getEnvironment(World world) {
        if(!cfg.isSet("Worlds." + world.getUID() + ".Environment"))
            return world.getEnvironment();
        return World.Environment.valueOf(cfg.getString("Worlds." + world.getUID() + ".Environment"));
    }

    public void backupWorld(Player player, World world) {
        var source = world.getWorldFolder().getPath();
        var destinationPath = world.getWorldFolder().getPath() + "/" + world.getName() + "-Backup.zip";
        var successMSG = "The world " + world.getName() + " was successfully backed up. \n Zip destination: " + destinationPath;

        player.sendMessage(Component.text("The backup process will begin shortly...", GREEN));
        player.sendMessage(Component.text("Saving world...", GREEN));
        world.save();
        player.sendMessage(Component.text("Saved the world.", GREEN));

        Bukkit.getScheduler().runTaskLaterAsynchronously(TommyGenerator.getInstance(), () -> {
            player.sendMessage(Component.text("Trying to save the world folder into the zip file..", GREEN));
            try {
                var oldBackup = new File(destinationPath);
                if(oldBackup.exists() && oldBackup.delete()) {
                    player.sendMessage(Component.text("Deleted old backup file."));
                }
                ZipManager.zip(source, destinationPath);
                player.sendMessage(Component.text(successMSG, GREEN));
            } catch (Exception e) {
                player.sendMessage(Component.text(e.getMessage(), RED));
            }
        }, 20L);
        new WorldCreator(world.getName()).createWorld();
    }
}
