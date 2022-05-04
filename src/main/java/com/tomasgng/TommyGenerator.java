package com.tomasgng;

import com.tomasgng.commands.TommyGeneratorCommand;
import com.tomasgng.listeners.InventoryClickListener;
import com.tomasgng.listeners.InventoryCloseListener;
import com.tomasgng.utils.GUIManager;
import com.tomasgng.utils.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.plugin.java.JavaPlugin;

public class TommyGenerator extends JavaPlugin {

    private static TommyGenerator instance;
    private WorldManager worldManager;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this;
        worldManager = new WorldManager();
        guiManager = new GUIManager();

        getCommand("tommygenerator").setExecutor(new TommyGeneratorCommand());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);

        WorldCreator worldCreator = WorldCreator.name("BeispielsWelt");
        worldCreator.type(WorldType.FLAT);
        worldCreator.createWorld().setSpawnLimit(SpawnCategory.ANIMAL, 0);
        worldCreator.createWorld().setSpawnLimit(SpawnCategory.MONSTER, 0);
    }

    public static TommyGenerator getInstance() {
        return instance;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public String getPluginVersion() {
        return getDescription().getVersion();
    }
}
