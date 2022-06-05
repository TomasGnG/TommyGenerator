package com.tomasgng.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TommyGeneratorCMDTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if(cmd.getName().equalsIgnoreCase("tommygenerator") || cmd.getName().equalsIgnoreCase("tg")) {
            if(args.length == 1) {
                if(sender.hasPermission("tommygenerator.use")) {
                    list.add("portalinfo");
                    return list;
                }

            }
        }
        return null;
    }
}
