package com.tomasgng;

import com.tomasgng.commands.TommyGeneratorCommand;
import com.tomasgng.listeners.InventoryClickListener;
import com.tomasgng.listeners.InventoryCloseListener;
import com.tomasgng.utils.GUIManager;
import com.tomasgng.utils.WorldManager;
import org.bukkit.Bukkit;
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

        registerCommandsAndListeners();
    }


    private void registerCommandsAndListeners() {
        var manager = Bukkit.getPluginManager();

        getCommand("tommygenerator").setExecutor(new TommyGeneratorCommand());

        manager.registerEvents(new InventoryClickListener(), this);
        manager.registerEvents(new InventoryCloseListener(), this);
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
