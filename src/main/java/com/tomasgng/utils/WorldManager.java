package com.tomasgng.utils;

import com.tomasgng.TommyGenerator;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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

    // World Creator

    public void createNewWorld(Player player, String worldName, World.Environment environment) {
        player.sendMessage("§aCreating world..");
        var creator = new WorldCreator(worldName);
        creator.environment(environment);
        creator.createWorld();
        addWorldToList(creator.name());
        player.sendMessage("§aCreating world.. Success");
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

}
