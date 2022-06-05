package com.tomasgng.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;


public class TommyPortal {

    public int ID;
    public World from;
    public World to;
    public List<Location> blocks;

    public TommyPortal(int _ID, World _from, World _to, List<Location> _blocks) {
        this.ID = _ID;
        this.from = _from;
        this.to = _to;
        this.blocks = _blocks;
    }
}
