package me.hugmanrique.noworldsave;

import javassist.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Main extends JavaPlugin {
    private static String PROVIDER_CLASS;

    static {
        String version = null;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            severe("Running custom NMS version, couldn't patch NMS classes");
        }

        if (version != null) {
            PROVIDER_CLASS = "net.minecraft.server." + version + ".ChunkProviderServer";
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        boolean restore = getConfig().getBoolean("restore");
        boolean changed = getConfig().getBoolean("changed");

        ClassPool pool = ClassPool.getDefault();
        CtClass providerClass;

        try {
            providerClass = pool.get(PROVIDER_CLASS);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return;
        }

        CtMethod saveMethod = getMethod(providerClass, "saveChunk");
        CtMethod nopMethod = getMethod(providerClass, "saveChunkNOP");

        if (restore) {
            restore(providerClass, saveMethod, nopMethod);
            return;
        }

        if (!changed) {
            patch(providerClass, saveMethod, nopMethod);
        }
    }

    private void patch(CtClass providerClass, CtMethod saveMethod, CtMethod nopMethod) {
        log("Patching original NMS chunk code...");

        try {
            clearMethod(saveMethod);
            clearMethod(nopMethod);

            // Replace runtime class
            providerClass.toClass();
        } catch (CannotCompileException e) {
            e.printStackTrace();
            severe("Couldn't replace NMS chunk saving methods, look above for more details/stacktraces");
        }

        getConfig().set("changed", true);
        saveConfig();

        log("Replaced NMS chunk saving methods");
    }

    private void restore(CtClass providerClass, CtMethod saveMethod, CtMethod nopMethod) {
        log("Restoring default NMS chunk saving. Make sure to remove the plugin!");

        try {
            saveMethod.setBody("if(this.chunkLoader!=null){ try{ chunk.setLastSaved(this.world.getTime()); this.chunkLoader.a(this.world,chunk); } catch(IOExceptionioexception){ ChunkProviderServer.b.error(\"Couldn'tsavechunk\",ioexception); } catch(ExceptionWorldConflictexceptionworldconflict){ ChunkProviderServer.b.error(\"Couldn'tsavechunk;alreadyinusebyanotherinstanceofMinecraft?\",exceptionworldconflict); } }");
            nopMethod.setBody("if (this.chunkLoader != null) { try { this.chunkLoader.b(this.world, chunk); } catch (Exception exception) { ChunkProviderServer.b.error(\"Couldn't save entities\", exception); } }");

            providerClass.toClass();
        } catch (CannotCompileException e) {
            e.printStackTrace();
            severe("Couldn't restore default NMS chunk saving implementation");
        }

        log("Restored default NMS chunk saving. You can now remove this plugin");
    }

    private CtMethod getMethod(CtClass ctClass, String name) {
        return Arrays.stream(ctClass.getMethods())
                .filter(method -> method.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    private void clearMethod(CtMethod method) throws CannotCompileException {
        method.setBody("{}");
    }

    private void log(String message) {
        getLogger().info(message);
    }

    private static void severe(String message) {
        Bukkit.getLogger().severe(message);
    }
}
