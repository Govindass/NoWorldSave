package me.hugmanrique.noworldsave;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.lang.reflect.Method;

public class Main extends JavaPlugin {
    private static String PROVIDER_CLASS;
    private static String CHUNK_CLASS;

    static {
        String version = null;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            severe("Running custom NMS version, couldn't patch NMS classes");
        }

        if (version != null) {
            PROVIDER_CLASS = "net/minecraft/server/" + version + "/ChunkProviderServer";
            CHUNK_CLASS = "net.minecraft.server." + version + ".Chunk";
        }
    }

    @Override
    public void onEnable() {
        log("Patching original NMS chunk code...");

        boolean result = patch();

        if (result) {
            log("Replaced NMS chunk saving methods");
        } else {
            log("Couldn't replace NMS chunk saving methods, look above for more details/stacktraces");
        }
    }

    public boolean patch() {
        Class<?> clazz;
        Class<?> chunkClass;

        try {
            clazz = Class.forName(PROVIDER_CLASS.replace("/", "."));
            chunkClass = Class.forName(CHUNK_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try {
            Method saveMethod = clazz.getMethod("saveChunk", chunkClass);
            Method saveNopMethod = clazz.getMethod("saveChunkNOP", chunkClass);

            disableMethod(saveMethod);
            disableMethod(saveNopMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private byte[] disableMethod(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        ClassReader reader;

        try {
            reader = new ClassReader(clazz.getResourceAsStream(clazz.getSimpleName() + ".class"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        // Passing the reader to the writer allows internal optimizations
        ClassWriter writer = new ClassWriter(reader, 0);

        reader.accept(new SaveRemoverMethod(writer, method.getName(), Type.getMethodDescriptor(method)), 0);

        // New code
        return writer.toByteArray();
    }

    private void log(String message) {
        getLogger().info(message);
    }

    private static void severe(String message) {
        Bukkit.getLogger().severe(message);
    }
}
