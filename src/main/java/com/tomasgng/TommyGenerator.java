package com.tomasgng;

import com.tomasgng.commands.TommyGeneratorCommand;
import com.tomasgng.listeners.*;
import com.tomasgng.tabcompleters.TommyGeneratorCMDTabCompleter;
import com.tomasgng.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public class TommyGenerator extends JavaPlugin {

    private static TommyGenerator instance;
    private WorldManager worldManager;
    private GUIManager guiManager;

    private PortalManager portalManager;

    @Override
    public void onEnable() {
        instance = this;
        worldManager = new WorldManager();
        guiManager = new GUIManager();
        portalManager = new PortalManager();

        int pluginId = 15315;
        Metrics metrics = new Metrics(this, pluginId);

        registerCommandsAndListeners();
        checkLatestVersion();
    }

    private void registerCommandsAndListeners() {
        var manager = Bukkit.getPluginManager();

        Objects.requireNonNull(getCommand("tommygenerator")).setExecutor(new TommyGeneratorCommand());
        Objects.requireNonNull(getCommand("tommygenerator")).setTabCompleter(new TommyGeneratorCMDTabCompleter());

        manager.registerEvents(new InventoryClickListener(), this);
        manager.registerEvents(new InventoryCloseListener(), this);
        manager.registerEvents(new PlayerChangedWorldListener(), this);
        manager.registerEvents(new PlayerTeleportListener(), this);
        manager.registerEvents(new PlayerQuitListener(), this);
        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new PortalCreateListener(), this);
        manager.registerEvents(new EntityPortalEnterListener(), this);
        manager.registerEvents(new PlayerPortalListener(), this);
        manager.registerEvents(new AsyncChatListener(), this);
        manager.registerEvents(new BlockBreakListener(), this);
    }

    private void checkLatestVersion() {
        var currentVersion = getPluginVersion();
        String latestVersion;

        try {
            var url = new URL("https://pastebin.com/raw/bBzxZi66");
            var scanner = new Scanner(url.openStream());
            var sb = new StringBuilder();
            while(scanner.hasNext()) {
                sb.append(scanner.next());
            }
            latestVersion = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(currentVersion.equalsIgnoreCase(latestVersion)) {
            Bukkit.getConsoleSender().sendMessage("§a[TommyGenerator] Using the latest version :D! [Version " + currentVersion + "]");
            Bukkit.getConsoleSender().sendMessage("§a[TommyGenerator] Thank you for using my plugin!! ;)");
            return;
        }
        Bukkit.getConsoleSender().sendMessage("§e[TommyGenerator] Using an outdated version(currently v" + currentVersion + ") :(! New version " + latestVersion );
        Bukkit.getConsoleSender().sendMessage("§a[TommyGenerator] Thank you for using my plugin!! ;)");
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

    public PortalManager getPortalManager() {
        return portalManager;
    }

    public String getPluginVersion() {
        return getDescription().getVersion();
    }
}
