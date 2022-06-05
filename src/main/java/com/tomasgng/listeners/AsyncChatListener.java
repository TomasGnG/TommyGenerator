package com.tomasgng.listeners;

import com.tomasgng.TommyGenerator;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener {
    @EventHandler
    public void on(AsyncChatEvent event) {
        var message = PlainTextComponentSerializer.plainText().serialize(event.message());
        if(TommyGenerator.getInstance().getPortalManager().isCreatingPortal(event.getPlayer())) {
            if(message.equalsIgnoreCase("cancel")) {
                TommyGenerator.getInstance().getPortalManager().togglePlayersCreatingState(event.getPlayer(), null, false);
                event.setCancelled(true);
            }
        }
    }
}
