package xiaoze_qwq_.eternal_firework_rocket.neoforge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.loot.ModLootTableModifier;
import xiaoze_qwq_.eternal_firework_rocket.platform.Platform;

import java.nio.file.Path;
import java.util.function.Consumer;

@Mod(EternalFireworkRocket.MOD_ID)
public class EternalFireworkRocketNeoForge implements Platform {

    public EternalFireworkRocketNeoForge(IEventBus modEventBus) {
        EternalFireworkRocket.init(this);
    }

    private static <T extends Event> void listen(Class<T> type, Consumer<T> handler) {
        NeoForge.EVENT_BUS.addListener(type, handler);
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public void onServerStarted(Consumer<MinecraftServer> callback) {
        listen(ServerStartedEvent.class, event -> callback.accept(event.getServer()));
    }

    @Override
    public void onServerStopped(Consumer<MinecraftServer> callback) {
        listen(ServerStoppingEvent.class, event -> callback.accept(event.getServer()));
    }

    @Override
    public void onServerTick(Consumer<MinecraftServer> callback) {
        listen(ServerTickEvent.Post.class, event -> callback.accept(event.getServer()));
    }

    @Override
    public void onPlayerJoin(Consumer<ServerPlayer> callback) {
        listen(PlayerEvent.PlayerLoggedInEvent.class, event -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                callback.accept(player);
            }
        });
    }

    @Override
    public void registerLootTableModifier() {
        listen(LootTableLoadEvent.class, event -> {
            if (("minecraft:" + ModLootTableModifier.END_CITY_TREASURE).equals(event.getName().toString())) {
                ModLootTableModifier.forEachPool(pool -> event.getTable().addPool(pool.build()));
            }
        });
    }
}
