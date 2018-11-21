package ml.eldub.tdg.events;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.clip.placeholderapi.PlaceholderAPI;
import ml.eldub.tdg.Main;
import ml.eldub.tdg.creator.Creator;
import ml.eldub.tdg.creator.TDG;
import ml.eldub.tdg.utils.Cooldown;
import ml.eldub.tdg.utils.Targeter;
import ml.eldub.tdg.utils.UpdateChecker;
import ml.eldub.tdg.utils.Utils;

public class PlayerEvents implements Listener {
	
	@SuppressWarnings("unused")
	private final Main plugin;
	  
	public PlayerEvents(Main plugin) {
	  this.plugin = plugin;
	}
	
	private TDG tdg = new TDG();
	private Utils utils = new Utils();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Main.getInstance().pmenu.put(player, "none");
        if (Main.getInstance().getConfig().getBoolean("options.update-checker") == true) {
        	if (player.hasPermission("tdg.checkupdates")) {
	            UpdateChecker updater = new UpdateChecker(Main.getInstance(), 61903);
	            try {
	                if (updater.checkForUpdates()) {
	                    player.sendMessage("§8[§cTDG§8] §bA new update of TDG §f§l(" + updater.getLatestVersion() + ") §bis available! Download it at §f§lhttps://www.spigotmc.org/resources/61903/");
	                }
	            }
	            catch (Exception ex) {
	                Logger log = Logger.getLogger("Minecraft");
	                log.severe("[TDG] Update checker failed, could not connect to the API.");
	                ex.printStackTrace();
	            }
        	}
        }
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		Entity en = e.getEntity();
		if (Main.getInstance().entities.contains(en)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
		Entity en = e.getRightClicked();
		if (Main.getInstance().entities.contains(en)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player player = e.getPlayer();
		if (tdg.hasMenuOpened(player)) {
			tdg.closeMenu(player);
		}
	}
	
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		String cmd = e.getMessage();
		for (String key : Main.getInstance().getMenuConfig().getKeys(false)) {
			String command = Main.getInstance().getMenuConfig().getString(key + ".command");
			if (cmd.equals("/" + command)) {
				e.setCancelled(true);
				if (!Main.getInstance().pmenu.get(player).equals(key)) {
					if (player.hasPermission("tdg.open." + key)) {
						Creator creator = new Creator(player, key, false);
						creator.open();
					}
					else {
    					player.sendMessage(Main.getInstance().getConfig().getString("messages.noPermission").replace("%prefix%", Main.getInstance().prefix).replace("&", "§"));
					}
				}
				else {
					player.sendMessage(Main.getInstance().getConfig().getString("messages.alreadyOpen").replace("%prefix%", Main.getInstance().prefix).replace("&", "§"));
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
			for (Entity en : Main.getInstance().entities) {
				Player player = e.getPlayer();
				if (Targeter.getTargetEntity(player) == en) {
					for (String icons : Main.getInstance().getMenuConfig().getConfigurationSection(Main.getInstance().pmenu.get(player) + ".icons").getKeys(false)) {
						String name = Main.getInstance().getMenuConfig().getString(Main.getInstance().pmenu.get(player) + ".icons." + icons + ".name").replace("%player%", player.getName()).replace("&", "§");
						if (Main.getInstance().papi == true) {
							name = PlaceholderAPI.setPlaceholders(player, name);
						}
						String action = Main.getInstance().getMenuConfig().getString(Main.getInstance().pmenu.get(player) + ".icons." + icons + ".click-action.action");
						String value = Main.getInstance().getMenuConfig().getString(Main.getInstance().pmenu.get(player) + ".icons." + icons + ".click-action.value").replace("%player%", player.getName()).replace("&", "§");
						if (Main.getInstance().papi == true) {
							value = PlaceholderAPI.setPlaceholders(player, value);
						}
						final String val = value;
						boolean cooldown = Main.getInstance().getMenuConfig().getBoolean(Main.getInstance().pmenu.get(player) + ".icons." + icons + ".click-cooldown.enabled");
						if (en.getName().equals(name)) {
							if (action.equals("NONE")) {
								utils.playSound(Main.getInstance().pmenu.get(player), icons, player);
								e.setCancelled(true);
							}
							if (action.equals("CLOSE")) {
								utils.playSound(Main.getInstance().pmenu.get(player), icons, player);
								tdg.closeMenu(player);
							}
							if (action.equals("OPEN_MENU")) {
								utils.playSound(Main.getInstance().pmenu.get(player), icons, player);
								new BukkitRunnable() {
									
									@Override
									public void run() {
										Creator creator = new Creator(player, val, true);
										creator.open();
										cancel();
									}
								}.runTaskTimer(Main.getInstance(), 1, 1);
							}
							if (action.equals("MESSAGE")) {
								if (!Cooldown.isInCooldown(player.getUniqueId(), icons)) {
									utils.playSound(Main.getInstance().pmenu.get(player), icons, player);
									player.sendMessage(value);
									if (cooldown == true) {
										if (!Cooldown.isInCooldown(player.getUniqueId(), icons)) {
											int time = Main.getInstance().getMenuConfig().getInt(Main.getInstance().pmenu.get(player) + ".icons." + icons + ".click-cooldown.time");
											Cooldown c = new Cooldown(player.getUniqueId(), icons, time);
											c.start();
										}
									}
								}
								else {
									int timeLeft = Cooldown.getTimeLeft(player.getUniqueId(), icons);
									player.sendMessage(Main.getInstance().getConfig().getString("messages.cooldown").replace("%prefix%", Main.getInstance().prefix).replace("%time%", Integer.toString(timeLeft)).replace("&", "§"));
								}
							}
							if (action.equals("COMMAND")) {
								if (!Cooldown.isInCooldown(player.getUniqueId(), icons)) {
									utils.playSound(Main.getInstance().pmenu.get(player), icons, player);
									String executefrom = Main.getInstance().getMenuConfig().getString(Main.getInstance().pmenu.get(player) + ".icons." + icons + ".click-action.executefrom");
									if (executefrom.equals("player")) {
										if (value.contains("op:")) {
											if (player.isOp()) {
												player.chat("/" + value.replace("op: ", ""));
											} else {
												player.setOp(true);
												player.chat("/" + value.replace("op: ", ""));
												player.setOp(false);
											}
										}
										else {
											player.chat("/" + value);
										}
									}
									if (executefrom.equals("console")) {
										Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), value);
									}
									if (cooldown == true) {
										if (!Cooldown.isInCooldown(player.getUniqueId(), icons)) {
											int time = Main.getInstance().getMenuConfig().getInt(Main.getInstance().pmenu.get(player) + ".icons." + icons + ".click-cooldown.time");
											Cooldown c = new Cooldown(player.getUniqueId(), icons, time);
											c.start();
										}
									}
								}
								else {
									int timeLeft = Cooldown.getTimeLeft(player.getUniqueId(), icons);
									player.sendMessage(Main.getInstance().getConfig().getString("messages.cooldown").replace("%prefix%", Main.getInstance().prefix).replace("%time%", Integer.toString(timeLeft)).replace("&", "§"));
								}
							}
						}
					}
				}
			}
		}
	}

}
