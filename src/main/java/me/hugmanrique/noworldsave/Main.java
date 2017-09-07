package me.hugmanrique.noworldsave;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        log("Patching original NMS chunk code...");

        boolean result = SavePatcher.patch();

        if (result) {
            log("Replaced NMS chunk saving methods");
        } else {
            log("Couldn't replace NMS chunk saving methods, look above for more details/stacktraces");
        }
    }

    private void log(String message) {
        getLogger().info(message);
    }
}
