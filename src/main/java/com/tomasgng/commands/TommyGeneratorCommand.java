package com.tomasgng.commands;

import com.tomasgng.TommyGenerator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TommyGeneratorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player player))
            return false;
        if(args.length == 0) {
            TommyGenerator.getInstance().getGuiManager().openMainInventory(player);
        } else if(args.length == 1 && args[0].equalsIgnoreCase("portalinfo")) {
            var portal = TommyGenerator.getInstance().getPortalManager().getPortal(Objects.requireNonNull(player.getTargetBlock(5)).getLocation());
            if(portal == null) {
                player.sendMessage("§cNot a portal!");
                return false;
            }
            player.sendMessage("§8____________________");
            player.sendMessage("§8");
            player.sendMessage("§8-> §7ID: §e" + portal.ID);
            player.sendMessage("§8-> §7Destination: §e" + portal.to.getName());
            player.sendMessage("§8");
            player.sendMessage("§8____________________");
        }
        return false;
    }
}
