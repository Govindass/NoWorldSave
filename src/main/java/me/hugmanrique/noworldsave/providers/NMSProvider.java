package me.hugmanrique.noworldsave.providers;

import me.hugmanrique.noworldsave.Provider;
import org.bukkit.Bukkit;

/**
 * @author Hugo Manrique
 * @since 03/12/2017
 */
public class NMSProvider implements Provider {
    private static final String SAVE_BODY = "if(this.chunkLoader!=null){ try{ chunk.setLastSaved(this.world.getTime()); this.chunkLoader.a(this.world,chunk); } catch(IOExceptionioexception){ ChunkProviderServer.b.error(\"Couldn'tsavechunk\",ioexception); } catch(ExceptionWorldConflictexceptionworldconflict){ ChunkProviderServer.b.error(\"Couldn'tsavechunk;alreadyinusebyanotherinstanceofMinecraft?\",exceptionworldconflict); } }";
    private static final String NOP_BODY = "if (this.chunkLoader != null) { try { this.chunkLoader.b(this.world, chunk); } catch (Exception exception) { ChunkProviderServer.b.error(\"Couldn't save entities\", exception); } }";

    @Override
    public String getClassName() throws IllegalStateException {
        String version;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalStateException(e);
        }

        return "net.minecraft.server." + version + ".ChunkProviderServer";
    }

    @Override
    public String getSaveMethodBody() {
        return SAVE_BODY;
    }

    @Override
    public String getNOPMethodBody() {
        return NOP_BODY;
    }
}
