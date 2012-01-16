/*
 * Copyright (C) 2011 MineStar.de 
 * 
 * This file is part of GreenMile.
 * 
 * GreenMile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * GreenMile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GreenMile.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.greenmile.listener;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.threading.BorderThread;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class GMPListener extends PlayerListener {

    private HashMap<String, Integer> map;

    public GMPListener(HashMap<String, Integer> map) {
        this.map = map;
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Location worldSpawn = event.getTo().getWorld().getSpawnLocation();
        int maxSize = 1500;
        if (map.containsKey(event.getTo().getWorld().getName()))
            maxSize = map.get(event.getTo().getWorld().getName());

        if (!BorderThread.isInside(event.getTo().getBlockX(), event.getTo().getBlockZ(), worldSpawn.getBlockX(), worldSpawn.getBlockZ(), maxSize)) {
            if (!event.getFrom().getWorld().getName().equalsIgnoreCase(event.getTo().getWorld().getName())) {
                event.getTo().setX(worldSpawn.getX());
                event.getTo().setY(worldSpawn.getY());
                event.getTo().setZ(worldSpawn.getZ());
                Player player = event.getPlayer();
                ChatUtils.printError(player, Main.name, "Du hast die maximale Grenze der Map erreicht!");
                ChatUtils.printInfo(player, Main.name, ChatColor.GRAY, "Du wurdest zum Spawn zurück teleportiert!");
            }
        }
    }
}
