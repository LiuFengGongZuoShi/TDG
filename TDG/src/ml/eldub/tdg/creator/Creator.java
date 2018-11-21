package ml.eldub.tdg.creator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.clip.placeholderapi.PlaceholderAPI;
import ml.eldub.tdg.Main;
import ml.eldub.tdg.utils.Utils;

public class Creator {
	
	private Player player;
	private String value;
	private boolean cm;
	
	Utils utils = new Utils();
	TDG menu = new TDG();
	
	public Creator(Player openTo, String toOpen, boolean changed) {
		
		player = openTo;
		value = toOpen;
		cm = changed;
	}
	
	public void open() {
		Location loc = null;
		if (cm == false) {
			loc = utils.getBFLoc(player.getLocation(), 3.5);
			Main.getInstance().lastLoc.put(player, loc);
		}
		if (cm == true) {
			loc = Main.getInstance().lastLoc.get(player);
		}
		if (Main.getInstance().getMenuConfig().contains(value)) {
			menu.closeMenu(player);
			Main.getInstance().pmenu.put(player, value);
			String openaction = Main.getInstance().getMenuConfig().getString(value + ".open-action");
			if (openaction.contains("sound")) {
				player.playSound(player.getLocation(), Sound.valueOf(openaction.replace("sound: ", "")), 1.0F, 1.0F);
			}
			if (openaction.contains("message")) {
				player.sendMessage(openaction.replace("message: ", "").replace("%player%", player.getName()).replace("&", "§"));
			}
			for (String icons : Main.getInstance().getMenuConfig().getConfigurationSection(value + ".icons").getKeys(false)) {
				String name = Main.getInstance().getMenuConfig().getString(value + ".icons." + icons + ".name").replace("%player%", player.getName()).replace("&", "§");
				if (Main.getInstance().papi == true) {
					name = PlaceholderAPI.setPlaceholders(player, name);
				}
				String type = Main.getInstance().getMenuConfig().getString(value + ".icons." + icons + ".icon-type");
				String url = Main.getInstance().getMenuConfig().getString(value + ".icons." + icons + ".value");
				String material = Main.getInstance().getMenuConfig().getString(value + ".icons." + icons + ".material");
				int materialdata = Main.getInstance().getMenuConfig().getInt(value + ".icons." + icons + ".material-data");
				int positionX = Main.getInstance().getMenuConfig().getInt(value + ".icons." + icons + ".positionX");
				int positionY = Main.getInstance().getMenuConfig().getInt(value + ".icons." + icons + ".positionY");
				double x1 = Main.getInstance().getMenuConfig().getDouble(value + ".distances.x1");
				double x2 = Main.getInstance().getMenuConfig().getDouble(value + ".distances.x2");
				double x4 = Main.getInstance().getMenuConfig().getDouble(value + ".distances.x4");
				double x5 = Main.getInstance().getMenuConfig().getDouble(value + ".distances.x5");
				if (type.equals("HEAD")) {
	    			menu.addIcon(player, utils.setPosition(loc, positionX, positionY, x1, x2, x4, x5), name, url, positionY);
				}
				if (type.equals("BLOCK")) {
					ItemStack item = new ItemStack(Material.getMaterial(material), 1, (short) materialdata);
	    			menu.addIcon(player, utils.setPosition(loc, positionX, positionY, x1, x2, x4, x5), name, item, positionY);
				}
				if (type.equals("ITEM")) {
					ItemStack item = new ItemStack(Material.getMaterial(material), 1, (short) materialdata);
	    			menu.addIcon(player, utils.setPosition(loc, positionX, positionY, x1, x2, x4, x5), name, item, true, positionY);
				}
				if (type.equals("TOOL")) {
					ItemStack item = new ItemStack(Material.getMaterial(material), 1, (short) materialdata);
	    			menu.addIcon(player, utils.setPosition(loc, positionX, positionY, x1, x2, x4, x5), name, item, true, true, positionY);
				}
			}
		}
	}

}
