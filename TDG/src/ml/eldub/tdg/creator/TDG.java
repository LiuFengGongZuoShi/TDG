package ml.eldub.tdg.creator;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import ml.eldub.tdg.Main;
import ml.eldub.tdg.utils.EntityHider;
import ml.eldub.tdg.utils.Metadata;
import ml.eldub.tdg.utils.EntityHider.Policy;
import ml.eldub.tdg.utils.Skull;
import ml.eldub.tdg.utils.Targeter;
import ml.eldub.tdg.utils.Utils;

public class TDG {
	
	public List<Player> view = new ArrayList<Player>();
	private List<Player> toHide = new ArrayList<Player>();
	private List<ArmorStand> icons = new ArrayList<ArmorStand>();
	String euler = "-1.1 1.5 1.4";
	
	private EntityHider entityHider = new EntityHider(Main.getInstance(), Policy.BLACKLIST);
	private Utils utils = new Utils();
	
	public void addIcon(Player p, Location loc, String name, ItemStack item, int positionY) {
		
		Vector playerDirection = p.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        loc.setDirection(direction);
        float yaw = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        float pitch = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        if (positionY == 2) {
        	loc.add(0.0, 1.5, 0.0);
        }
		ArmorStand a = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		a.setVisible(false);
		a.setCustomName(name.replace("&", "§"));
		a.setCustomNameVisible(true);
		a.setHelmet(item);
		a.setArms(true);
		icons.add(a);
		view.add(p);
		Main.getInstance().entities.add(a);
		Metadata.set(a, p.getName(), p);
		Location locb = a.getLocation();
		toHide.clear();
		for (Player all : Bukkit.getOnlinePlayers()) {
			toHide.add(all);
			toHide.remove(p);
		}
		for (Player hide : toHide) {
		    entityHider.hideEntity(hide, a);
		}
	    
	    new BukkitRunnable() {
	        	
	        @Override
	        public void run() {
	        	if (a.isValid()) {
	        		a.teleport(locb);
	        		a.setFireTicks(0);
	                if (Targeter.getTargetEntity(p) == a) {
	                	a.setGravity(true);
		                a.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		                a.teleport(locb);
	                } else {
	                    a.setGravity(false);
	                }
					if (p.getLocation().distanceSquared(a.getLocation()) >= 120) {
						view.remove(p);
					}
					if (!p.isOnline()) {
						Main.getInstance().entities.remove(a);
						a.remove();
						view.remove(p);
					}
	        		if (!view.contains(p)) {
	        			Main.getInstance().entities.remove(a);
	        			a.remove();
	        			Main.getInstance().pmenu.put(p, "none");
	        		}
	        	}
	        }
	    }.runTaskTimer(Main.getInstance(), 0, 0);
	}
	
	public void addIcon(Player p, Location loc, String name, String texture, int positionY) {
		
		Vector playerDirection = p.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        loc.setDirection(direction);
        float yaw = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        float pitch = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        if (positionY == 2) {
        	loc.add(0.0, 1.5, 0.0);
        }
		ArmorStand a = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		a.setVisible(false);
		a.setCustomName(name.replace("&", "§"));
		a.setCustomNameVisible(true);
		if (texture.contains("textures.minecraft.net")) {
			a.setHelmet(Skull.getSkull(texture));
		}
		else {
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(texture.replace("%player%", p.getName()));
			skull.setItemMeta(meta);
			a.setHelmet(skull);
		}
		a.setArms(true);
		icons.add(a);
		view.add(p);
		Main.getInstance().entities.add(a);
		Metadata.set(a, p.getName(), p);
		Location locb = a.getLocation();
		toHide.clear();
		for (Player all : Bukkit.getOnlinePlayers()) {
			toHide.add(all);
			toHide.remove(p);
		}
		for (Player hide : toHide) {
		    entityHider.hideEntity(hide, a);
		}
	    
	    new BukkitRunnable() {
	        	
	        @Override
	        public void run() {
	        	if (a.isValid()) {
	        		a.teleport(locb);
	        		a.setFireTicks(0);
	                if (Targeter.getTargetEntity(p) == a) {
	                	a.setGravity(true);
		                a.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		                a.teleport(locb);
	                } else {
	                    a.setGravity(false);
	                }
					if (p.getLocation().distanceSquared(a.getLocation()) >= 120) {
						view.remove(p);
					}
					if (!p.isOnline()) {
						Main.getInstance().entities.remove(a);
						a.remove();
						view.remove(p);
					}
	        		if (!view.contains(p)) {
	        			Main.getInstance().entities.remove(a);
	        			a.remove();
	        			Main.getInstance().pmenu.put(p, "none");
	        		}
	        	}
	        }
	    }.runTaskTimer(Main.getInstance(), 0, 0);
	}
	
	public void addIcon(Player p, Location loc, String name, ItemStack item, boolean hand, int positionY) {
		
		Vector playerDirection = p.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        loc.setDirection(direction);
        float yaw = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        float pitch = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        if (positionY == 2) {
        	loc.add(0.0, 1.5, 0.0);
        }
		ArmorStand a = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		Vector v = a.getLocation().getDirection();
		Vector v2 = v.clone().setX(v.getZ()).setZ(-v.getX());
		Location locs = a.getLocation();
		locs.setDirection(v2);
		ArmorStand a2 = (ArmorStand) p.getWorld().spawnEntity(locs, EntityType.ARMOR_STAND);
		a.setVisible(false);
		a.setCustomName(name.replace("&", "§"));
		a.setCustomNameVisible(true);
		a2.setVisible(false);
		a2.setCustomName(name.replace("&", "§"));
		a2.setRightArmPose(new EulerAngle(4.7, 4.8, 6.3));
		a2.setItemInHand(item);
		a.setArms(true);
		a2.setArms(true);
		icons.add(a);
		icons.add(a2);
		view.add(p);
		Main.getInstance().entities.add(a);
		Main.getInstance().entities.add(a2);
		Metadata.set(a, p.getName(), p);
		Metadata.set(a2, p.getName(), p);
		Location locb = a.getLocation();
		toHide.clear();
		for (Player all : Bukkit.getOnlinePlayers()) {
			toHide.add(all);
			toHide.remove(p);
		}
		for (Player hide : toHide) {
		    entityHider.hideEntity(hide, a);
		    entityHider.hideEntity(hide, a2);
		}
	    
	    new BukkitRunnable() {
	        	
	        @Override
	        public void run() {
	        	if (a.isValid()) {
	        		a.teleport(locb);
	        		a2.teleport(locs);
	        		a.setFireTicks(0);
	        		a2.setFireTicks(0);
	                if (Targeter.getTargetEntity(p) == a || (Targeter.getTargetEntity(p) == a2)) {
	                	a.setGravity(true);
		                a.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		                a.teleport(locb);
	                	a2.setGravity(true);
		                a2.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		        		a2.teleport(locs);
	                } else {
	                    a.setGravity(false);
	                    a2.setGravity(false);
	                }
					if (p.getLocation().distanceSquared(a.getLocation()) >= 120) {
						view.remove(p);
					}
					if (!p.isOnline()) {
						Main.getInstance().entities.remove(a);
						Main.getInstance().entities.remove(a2);
						a.remove();
						a2.remove();
						view.remove(p);
					}
	        		if (!view.contains(p)) {
	        			Main.getInstance().entities.remove(a);
	        			Main.getInstance().entities.remove(a2);
	        			a.remove();
	        			a2.remove();
	        			Main.getInstance().pmenu.put(p, "none");
	        		}
	        	}
	        }
	    }.runTaskTimer(Main.getInstance(), 0, 0);
	}
	
	public void addIcon(Player p, Location loc, String name, ItemStack item, boolean hand, boolean tool, int positionY) {
		
		Vector playerDirection = p.getLocation().getDirection();
        Vector direction = playerDirection.normalize();
        direction.multiply(-2);
        loc.setDirection(direction);
        float yaw = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        float pitch = (float)Math.toDegrees(Math.atan2(p.getLocation().getZ() - loc.getZ(), p.getLocation().getX() - loc.getX())) - 90;
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        if (positionY == 2) {
        	loc.add(0.0, 1.5, 0.0);
        }
		ArmorStand a = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		Location locs = utils.getLeftSide(a.getLocation().add(0.0, -0.3, 0.0), 0.4);
		ArmorStand a2 = (ArmorStand) p.getWorld().spawnEntity(locs, EntityType.ARMOR_STAND);
		a.setVisible(false);
		a.setCustomName(name.replace("&", "§"));
		a.setCustomNameVisible(true);
		a2.setVisible(false);
		a2.setCustomName(name.replace("&", "§"));
		a2.setRightArmPose(new EulerAngle(-1.1, 1.7, 1.4));
		a2.setItemInHand(item);
		a.setArms(true);
		a2.setArms(true);
		icons.add(a);
		icons.add(a2);
		view.add(p);
		Main.getInstance().entities.add(a);
		Main.getInstance().entities.add(a2);
		Metadata.set(a, p.getName(), p);
		Metadata.set(a2, p.getName(), p);
		Location locb = a.getLocation();
		toHide.clear();
		for (Player all : Bukkit.getOnlinePlayers()) {
			toHide.add(all);
			toHide.remove(p);
		}
		for (Player hide : toHide) {
		    entityHider.hideEntity(hide, a);
		    entityHider.hideEntity(hide, a2);
		}
	    
	    new BukkitRunnable() {
	        	
	        @Override
	        public void run() {
	        	if (a.isValid()) {
	        		a.teleport(locb);
	        		a2.teleport(locs);
	        		a.setFireTicks(0);
	        		a2.setFireTicks(0);
	                if (Targeter.getTargetEntity(p) == a || (Targeter.getTargetEntity(p) == a2)) {
	                	a.setGravity(true);
		                a.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		                a.teleport(locb);
	                	a2.setGravity(true);
		                a2.setVelocity(p.getLocation().toVector().subtract(a.getLocation().toVector()).multiply(0.1));
		        		a2.teleport(locs);
	                } else {
	                    a.setGravity(false);
	                    a2.setGravity(false);
	                }
					if (p.getLocation().distanceSquared(a.getLocation()) >= 120) {
						view.remove(p);
					}
					if (!p.isOnline()) {
						Main.getInstance().entities.remove(a);
						Main.getInstance().entities.remove(a2);
						a.remove();
						a2.remove();
						view.remove(p);
					}
	        		if (!view.contains(p)) {
	        			Main.getInstance().entities.remove(a);
	        			Main.getInstance().entities.remove(a2);
	        			a.remove();
	        			a2.remove();
	        			Main.getInstance().pmenu.put(p, "none");
	        		}
	        	}
	        }
	    }.runTaskTimer(Main.getInstance(), 0, 0);
	}
	
	public boolean hasMenuOpened(Player player) {
		if (view.contains(player)) {
			return true;
		}
		return false;
	}
	
	public void closeMenu(Player player) {
		for (Entity en : player.getWorld().getEntities()) {
			if (Metadata.hasKey(en, player.getName())) {
				en.remove();
			}
		}
		view.remove(player);
		Main.getInstance().pmenu.put(player, "none");
	}
}
