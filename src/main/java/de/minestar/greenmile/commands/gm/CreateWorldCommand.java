package de.minestar.greenmile.commands.gm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import de.minestar.greenmile.helper.EnumHelper;
import de.minestar.greenmile.worlds.WorldManager;
import de.minestar.minstarlibrary.commands.ExtendedCommand;
import de.minestar.minstarlibrary.utils.ChatUtils;

public class CreateWorldCommand extends ExtendedCommand {
    private WorldManager worldManager;

    public CreateWorldCommand(String pluginName, String syntax, String arguments, String node, WorldManager worldManager) {
        super(pluginName, syntax, arguments, node);
        this.description = "Create a new world";
        this.worldManager = worldManager;
    }

    public void execute(String[] args, CommandSender sender) {
        Long seed = 1337l;
        Environment environment = Environment.NORMAL;

        String worldName = args[0];

        if (args.length >= 2) {
            environment = EnumHelper.getEnvironment(args[1]);
            if (environment == null) {
                ChatUtils.printInfo(sender, "[GreenMile]", ChatColor.GRAY, "Environment '" + args[1] + "' not found");
                return;
            }

        }

        if (args.length >= 3) {
            try {
                seed = Long.valueOf(Long.parseLong(args[2]));
            } catch (Exception e) {
                seed = Long.valueOf(args[2].hashCode());
            }

        }

        if ((this.worldManager.worldExists(worldName)) || (Bukkit.getServer().getWorld(worldName) != null)) {
            ChatUtils.printError(sender, this.pluginName, "Error while creating world '" + worldName + "'!");
            ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, "A world with that name does already exist.");
            return;
        }

        boolean result = this.worldManager.createWorld(worldName, environment, seed.longValue(), this.worldManager.getDataFolder());

        if (result) {
            ChatUtils.printSuccess(sender, this.pluginName, "World '" + worldName + "' created!");
        } else {
            ChatUtils.printError(sender, this.pluginName, "Error while creating world '" + worldName + "'!");
            ChatUtils.printInfo(sender, this.pluginName, ChatColor.GRAY, "There was an internal error while creating the worldsettings.");
        }
    }
}