package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PlayerChangedWorldListener implements Listener {

    public static Map<Player, Collection<PotionEffect>> addedEffects = new HashMap<>();
    @EventHandler
    public void on(PlayerChangedWorldEvent event) {
        var worldManager = TommyGenerator.getInstance().getWorldManager();
        var player = event.getPlayer();
        var world = player.getWorld();
        if(!worldManager.getGameMode(world).equals("DISABLED") && !worldManager.getGameMode(world).equals("NOT SET")) {
            if(!player.hasPermission("tommygenerator.worldgamemode.bypass")) {
                Objects.requireNonNull(player.getPlayer()).setGameMode(GameMode.valueOf(worldManager.getGameMode(event.getPlayer().getWorld())));
            }
        }
        if(addedEffects.containsKey(player)) {
            var playerPreviousPotionEffects = addedEffects.get(player);
            for (var potionEffect : playerPreviousPotionEffects) {
                player.removePotionEffect(potionEffect.getType());
            }
            addedEffects.remove(player);
        }
        var potionEffectCollection = new ArrayList<PotionEffect>();
        for (int i = 0; i < worldManager.getActiveEffects(world).size(); i++) {
            if(!player.hasPotionEffect(Objects.requireNonNull(PotionEffectType.getByName(worldManager.getActiveEffects(world).get(i))))) {
                potionEffectCollection.add(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(worldManager.getActiveEffects(world).get(i))), 9999999, 0, false, false));
            }
        }
        player.addPotionEffects(potionEffectCollection);
        addedEffects.put(player, potionEffectCollection);
    }
}
