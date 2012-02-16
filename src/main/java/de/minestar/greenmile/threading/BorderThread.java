package de.minestar.greenmile.threading;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class BorderThread implements Runnable {

    private HashMap<String, Location> lastPosition = new HashMap<String, Location>();
    private final WorldManager worldManager;

    public BorderThread(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    public void run() {
        Location loc = null;

        int xP = 0;
        int zP = 0;

        int xW = 0;
        int zW = 0;

        int maxSize = 0;

        String worldName = null;
        for (World world : Bukkit.getWorlds()) {
            worldName = world.getName();

            // World is not existing -> ignore
            if (!worldManager.worldExists(worldName))
                continue;

            // World Spawn Location
            loc = worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn();

            xW = loc.getBlockX();
            zW = loc.getBlockZ();

            // Allowed size of the world
            maxSize = worldManager.getGMWorld(worldName).getWorldSettings().getMaxSize();

            // look at every players location
            for (Player player : Bukkit.getWorld(worldName).getPlayers()) {
                if ((player.isDead()) || (!player.isOnline()))
                    continue;

                // Players location
                loc = player.getLocation();
                xP = loc.getBlockX();
                zP = loc.getBlockZ();

                // player has left the world
                if (!isInside(xP, zP, xW, zW, maxSize)) {
                    // get last position which is inside the world to teleport
                    // him
                    loc = lastPosition.get(player.getName());

                    // have reconnected or last position is the current position
                    // -> teleport to spawn
                    if ((loc == null) || (!isInside(loc.getBlockX(), loc.getBlockZ(), xW, zW, maxSize)))
                        loc = worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn();

                    player.teleport(loc);
                    PlayerUtils.sendError(player, Main.NAME, "Du hast die maximale Grenze der Map erreicht!");
                } else
                    // save last position
                    lastPosition.put(player.getName(), loc);
            }
        }
    }

    public static boolean isInside(int xP, int zP, int xW, int zW, int width) {
        return (xP < xW + width) && (xP > xW - width) && (zP < zW + width) && (zP > zW - width);
    }
}