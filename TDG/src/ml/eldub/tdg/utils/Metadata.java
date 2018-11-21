package ml.eldub.tdg.utils;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import ml.eldub.tdg.Main;

public class Metadata {
	
    public static void set(Entity e, String key, Object value) {
        e.setMetadata(key, new FixedMetadataValue(Main.getInstance(), value));
    }
 
    public static void remove(Entity e, String key) {
        if (hasKey(e, key)) {
            e.removeMetadata(key, Main.getInstance());
        }
    }
 
    public static boolean hasKey(Entity e, String key) {
        if (e.getMetadata(key).size() == 0) {
            return false;
        }
        return true;
    }
 
    public Object get(Entity e, String key) {
        return e.getMetadata(key).get(0).value();
    }
 
    public void removeAll(Entity e, String[] keys) {
        for (String k : keys) {
            remove(e, k);
        }
    }

}
