package com.tomasgng;

import com.tomasgng.commands.TommyGeneratorCommand;
import com.tomasgng.listeners.*;
import com.tomasgng.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class TommyGenerator extends JavaPlugin {

    private static TommyGenerator instance;
    private WorldManager worldManager;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this;
        worldManager = new WorldManager();
        guiManager = new GUIManager();

        int pluginId = 15315;
        Metrics metrics = new Metrics(this, pluginId);

        registerCommandsAndListeners();
    }

    private void registerCommandsAndListeners() {
        var manager = Bukkit.getPluginManager();

        Objects.requireNonNull(getCommand("tommygenerator")).setExecutor(new TommyGeneratorCommand());

        manager.registerEvents(new InventoryClickListener(), this);
        manager.registerEvents(new InventoryCloseListener(), this);
        manager.registerEvents(new PlayerChangedWorldListener(), this);
        manager.registerEvents(new PlayerTeleportListener(), this);
        manager.registerEvents(new PlayerQuitListener(), this);
        manager.registerEvents(new PlayerJoinListener(), this);
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
