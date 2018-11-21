package ml.eldub.tdg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ml.eldub.tdg.commands.PlayerCommand;
import ml.eldub.tdg.events.PlayerEvents;
import ml.eldub.tdg.utils.UpdateChecker;

public class Main extends JavaPlugin {
	
	private static Main m;
	public static Main getInstance() {
		return m;
	}
	
	public String ver = this.getDescription().getVersion();
	public String prefix = getConfig().getString("messages.prefix").replace("&", "§");
	public boolean papi = false;
	
	public List<Entity> entities = new ArrayList<Entity>();
	public HashMap<Player, String> pmenu = new HashMap<Player, String>();
	public HashMap<Player, Location> lastLoc = new HashMap<Player, Location>();
	
	public void onEnable() {
		
		m = this;
		registerCommands();
		Bukkit.getConsoleSender().sendMessage("[TDG] v" + ver + " by ElDub");
		Bukkit.getPluginManager().registerEvents(new PlayerEvents(this), this);
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			this.papi = true;
			Bukkit.getConsoleSender().sendMessage("[TDG] PlaceholderAPI hooked!");
		}
		checkUpdates();
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
	    this.menub = new File(getDataFolder(), "menu.yml");
	    if (!this.menub.exists()) {
	        this.menub.getParentFile().mkdirs();
	        saveResource("menu.yml", false);
	    }
	    this.menu = new YamlConfiguration();
	    try {
	        try {
	            this.menu.load(this.menub);
	        }
	        catch (InvalidConfigurationException e) {
	            e.printStackTrace();
	        }
	        getConfig().options().copyDefaults(true);
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    for (String key : getMenuConfig().getKeys(false)) {
	    	if (getMenuConfig().getString(key + ".open-action").contains("ENDERMAN_TELEPORT")) {
	    		if (Bukkit.getVersion().contains("1.9") || (Bukkit.getVersion().contains("1.10") 
	    				|| (Bukkit.getVersion().contains("1.11") || (Bukkit.getVersion().contains("1.12"))))) {
	    			getMenuConfig().set(key + ".open-action", "sound: ENTITY_ENDERMEN_TELEPORT");
	    		}
	    	}
	    	for (String icons : getMenuConfig().getConfigurationSection(key + ".icons").getKeys(false)) {
	    		if (getMenuConfig().getString(key + ".icons." + icons + ".click-sound.sound").equals("CHICKEN_EGG_POP")) {
		    		if (Bukkit.getVersion().contains("1.9") || (Bukkit.getVersion().contains("1.10") 
		    				|| (Bukkit.getVersion().contains("1.11") || (Bukkit.getVersion().contains("1.12"))))) {
		    			getMenuConfig().set(key + ".icons." + icons + ".click-sound.sound", "ENTITY_CHICKEN_EGG");
		    		}
	    		}
	    	}
	    }
	}
	
	public void onDisable() {
		for (Entity en : entities) {
			en.remove();
		}
	}
	
	public void registerCommands() {
		getCommand("tdg").setExecutor(new PlayerCommand());
	}
    
    FileConfiguration menu;
    File menub;
    
    public FileConfiguration getMenuConfig() {
        return this.menu;
    }
    
    public void saveMenuConfig() {
        try {
            this.menu.save(this.menub);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void reloadMenuConfig() {
	    this.menu = new YamlConfiguration();
	    try {
	        try {
	            this.menu.load(this.menub);
	        }
	        catch (InvalidConfigurationException e) {
	            e.printStackTrace();
	        }
	        getConfig().options().copyDefaults(true);
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
    }
    
    public void checkUpdates() {
        if (this.getConfig().getBoolean("options.update-checker") == true) {
            Bukkit.getConsoleSender().sendMessage("[TDG] Checking for updates...");
            UpdateChecker updater = new UpdateChecker(this, 61903);
            try {
                if (updater.checkForUpdates()) {
                    Bukkit.getConsoleSender().sendMessage("[TDG] A new update of TDG (" + updater.getLatestVersion() + ") is available! Download it at https://www.spigotmc.org/resources/61903/");
                } else {
                    Bukkit.getConsoleSender().sendMessage("[TDG] You are running the latest version!");
                }
            }
            catch (Exception e) {
                Logger log = Logger.getLogger("Minecraft");
                log.severe("[TDG] Update checker failed, could not connect to the API.");
                e.printStackTrace();
            }
        }
    }

}
