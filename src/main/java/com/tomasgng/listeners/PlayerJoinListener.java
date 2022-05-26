package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void on(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var world = player.getWorld();
        var worldEffects = TommyGenerator.getInstance().getWorldManager().getActiveEffects(world);

        var potionEffectCollection = new ArrayList<PotionEffect>();
        for (String worldEffect : worldEffects) {
            if (!player.hasPotionEffect(Objects.requireNonNull(PotionEffectType.getByName(worldEffect)))) {
                potionEffectCollection.add(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(worldEffect)), 9999999, 0, false, false));
            }
        }
        player.addPotionEffects(potionEffectCollection);
    }
}
