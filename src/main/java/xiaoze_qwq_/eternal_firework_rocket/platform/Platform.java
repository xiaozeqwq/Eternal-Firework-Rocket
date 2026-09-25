package xiaoze_qwq_.eternal_firework_rocket.platform;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Loader-specific hooks used by the common code. One implementation exists per
 * loader and is only compiled into that loader's jar.
 */
public interface Platform {

    /** Directory in which the mod stores its user configuration. */
    Path getConfigDir();

    void onServerStarted(Consumer<MinecraftServer> callback);

    void onServerStopped(Consumer<MinecraftServer> callback);

    void onServerTick(Consumer<MinecraftServer> callback);

    void onPlayerJoin(Consumer<ServerPlayer> callback);

    /** Hooks the loader's loot table event and forwards matching tables to {@code ModLootTableModifier}. */
    void registerLootTableModifier();
}
