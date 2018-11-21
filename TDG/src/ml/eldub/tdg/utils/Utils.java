package ml.eldub.tdg.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import ml.eldub.tdg.Main;

public class Utils {
	
	Utils utils = this;
	
	public void playSound(String menu, String icon, Player player) {
		if (Main.getInstance().getMenuConfig().getBoolean(menu + ".icons." + icon + ".click-sound.enabled") == true) {
			String sound = Main.getInstance().getMenuConfig().getString(menu + ".icons." + icon + ".click-sound.sound");
			int pitch = Main.getInstance().getMenuConfig().getInt(menu + ".icons." + icon + ".click-sound.pitch");
			player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0F, pitch);
		}
	}
	
	public Location setPosition(Location loc, int positionX, int positionY, double x1, double x2, double x4, double x5) {
		
		if (positionX == 1) {
			return utils.getBFLoc(utils.getLeftSide(loc, x1), -1.0);
		}
		if (positionX == 2) {
			return utils.getBFLoc(utils.getLeftSide(loc, x2), -0.5);
		}
		if (positionX == 3) {
			return utils.getLeftSide(loc, 0);
		}
		if (positionX == 4) {
			return utils.getBFLoc(utils.getRightSide(loc, x4), -0.5);
		}
		if (positionX == 5) {
			return utils.getBFLoc(utils.getRightSide(loc, x5), -1.0);
		}
		return null;
	}
    
    /**
    * Returns a location with a specified distance away from the right side of
    * a location.
    *
    * @param location The origin location
    * @param distance The distance to the right
    * @return the location of the distance to the right
    */
    public Location getRightSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }

    /**
    * Gets a location with a specified distance away from the left side of a
    * location.
    *
    * @param location The origin location
    * @param distance The distance to the left
    * @return the location of the distance to the left
    */
    public Location getLeftSide(Location location, double distance) {
        float angle = location.getYaw() / 60;
        return location.clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
    }
    
    /**
    * Gets a location with a specified distance away from the behind or front
    * from a location.
    */
    public Location getBFLoc(Location loc, double distance) {
        Vector inverseDirectionVec = loc.getDirection().normalize().multiply(distance);
        return loc.add(inverseDirectionVec);
    }

}
