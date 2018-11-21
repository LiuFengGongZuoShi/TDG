package ml.eldub.tdg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ml.eldub.tdg.Main;
import ml.eldub.tdg.creator.Creator;

public class PlayerCommand implements CommandExecutor {
	
    public boolean onCommand(CommandSender sender, final Command cmd, String label, String[] args) {
    	
    	if (sender instanceof Player) {
    		if (cmd.getName().equals("tdg")) {
    			Player player = (Player)sender;
    			if (args.length == 0) {
    				if (player.hasPermission("tdg.use")) {
	    				player.sendMessage("§9TDG §8- §ev" + Main.getInstance().ver);
	    				player.sendMessage("§7/tdg open §e<menu> §8- §7Open a menu");
	    				player.sendMessage("§7/tdg list §8- §7Display the list of menus");
	    				player.sendMessage("§7/tdg reload §8- §7Reload plugin configuration");
    				}
    				else {
    					player.sendMessage(Main.getInstance().getConfig().getString("messages.noPermission").replace("%prefix%", Main.getInstance().prefix).replace("&", "§"));
    				}
    			}
    			if (args.length == 1) {
    				switch (args[0].toLowerCase()) {
    				case "open":
    					player.sendMessage(Main.getInstance().prefix + " §c/tdg open <menu>");
    					break;
    				case "list":
    					if (player.hasPermission("tdg.list")) {
	        				player.sendMessage(Main.getInstance().prefix + " §7Available menus:");
	    					for (String key : Main.getInstance().getMenuConfig().getKeys(false)) {
	    						player.sendMessage("§e" + key);
	    					}
    					}
    					else {
        					player.sendMessage(Main.getInstance().getConfig().getString("messages.noPermission").replace("%prefix%", Main.getInstance().prefix).replace("&", "§"));
    					}
    					break;
    				case "reload":
    					if (player.hasPermission("tdg.reload")) {
	    					Main.getInstance().reloadConfig();
	    					Main.getInstance().reloadMenuConfig();
	    					player.sendMessage(Main.getInstance().prefix + " §aConfig reloaded correctly.");
    					}
    					else {
        					player.sendMessage(Main.getInstance().getConfig().getString("messages.noPermission").replace("%prefix%", Main.getInstance().prefix).replace("&", "§"));
    					}
    					break;
    				default:
    					player.sendMessage(Main.getInstance().getConfig().getString("messages.invalidArgument").replace("%prefix%", Main.getInstance().prefix).replace("&", "§"));
    					break;
    				}
    			}
    			if (args.length == 2) {
    				if (args[0].equals("open")) {
    					if (Main.getInstance().getMenuConfig().contains(args[1])) {
	    					if (player.hasPermission("tdg.open." + args[1])) {
	    						if (!Main.getInstance().pmenu.get(player).equals(args[1])) {
			    					Creator c = new Creator(player, args[1], false);
			    					c.open();
	    						}
		    					else {
		        					player.sendMessage(Main.getInstance().getConfig().getString("messages.alreadyOpen").replace("%prefix%", Main.getInstance().prefix).replace("&", "§"));
		    					}
	    					}
	    					else {
	        					player.sendMessage(Main.getInstance().getConfig().getString("messages.noPermission").replace("%prefix%", Main.getInstance().prefix).replace("&", "§"));
	    					}
    					}
    					else {
        					player.sendMessage(Main.getInstance().getConfig().getString("messages.invalidMenu").replace("%prefix%", Main.getInstance().prefix).replace("%menu%", args[1]).replace("&", "§"));
    					}
    				}
    			}
    		}
    	}
    	else {
    		sender.sendMessage("[TDG] This command is only executable for players!");
    	}
    	return false;
    }

}
