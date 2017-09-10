package me.hugmanrique.noworldsave;

import org.bukkit.Bukkit;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class SavePatcher {
    private static final Logger LOGGER = Bukkit.getLogger();

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

    public static boolean patch() {
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

    private static byte[] disableMethod(Method method) {
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

    private static void severe(String message) {
        LOGGER.severe(message);
    }
}
