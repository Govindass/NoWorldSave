package me.hugmanrique.noworldsave;

import javassist.*;
import me.hugmanrique.noworldsave.providers.NMSProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Main extends JavaPlugin {
    private static final Provider PROVIDER = new NMSProvider();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        boolean restore = getConfig().getBoolean("restore");
        CtClass providerClass = getProviderClass();

        if (providerClass == null) {
            return;
        }

        CtMethod saveMethod = getMethod(providerClass, "saveChunk");
        CtMethod nopMethod = getMethod(providerClass, "saveChunkNOP");

        if (restore) {
            restore(providerClass, saveMethod, nopMethod);
        } else {
            patch(providerClass, saveMethod, nopMethod);
        }
    }

    private CtClass getProviderClass() {
        String className;

        try {
            className = PROVIDER.getClassName();
        } catch (IllegalStateException e) {
            severe("Running custom NMS version, couldn't patch NMS classes");
            return null;
        }

        ClassPool pool = ClassPool.getDefault();

        try {
            return pool.get(className);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return null;
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

        log("Replaced NMS chunk saving methods");
    }

    private void restore(CtClass providerClass, CtMethod saveMethod, CtMethod nopMethod) {
        log("Restoring default NMS chunk saving. Make sure to remove the plugin!");

        try {
            saveMethod.setBody(PROVIDER.getSaveMethodBody());
            nopMethod.setBody(PROVIDER.getNOPMethodBody());

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

    private void severe(String message) {
        getLogger().severe(message);
    }
}
