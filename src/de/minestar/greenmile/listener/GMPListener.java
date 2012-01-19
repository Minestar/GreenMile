package de.minestar.greenmile.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.minestar.greenmile.Main;
import de.minestar.greenmile.threading.BorderThread;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class GMPListener extends PlayerListener {
    private final WorldManager worldManager;

    public GMPListener(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    public void onPlayerTeleport(PlayerTeleportEvent event) {
        String worldName = event.getTo().getWorld().getName();

        if (!this.worldManager.worldExists(worldName)) {
            return;
        }
        Location worldSpawn = this.worldManager.getGMWorld(worldName).getWorldSettings().getWorldSpawn();
        int maxSize = this.worldManager.getGMWorld(worldName).getWorldSettings().getMaxSize();

        if ((!BorderThread.isInside(event.getTo().getBlockX(), event.getTo().getBlockZ(), worldSpawn.getBlockX(), worldSpawn.getBlockZ(), maxSize)) && (!event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName()))) {
            event.setTo(worldSpawn.clone());
            Player player = event.getPlayer();
            ChatUtils.printError(player, Main.name, "Du hast die maximale Grenze der Map erreicht!");
            ChatUtils.printInfo(player, Main.name, ChatColor.GRAY, "Du wurdest zum Spawn zurück teleportiert!");
        }
    }
}