package xiaoze_qwq_.eternal_firework_rocket.forge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.loot.ModLootTableModifier;
import xiaoze_qwq_.eternal_firework_rocket.platform.Platform;

import java.nio.file.Path;
import java.util.function.Consumer;

@Mod(EternalFireworkRocket.MOD_ID)
public class EternalFireworkRocketForge implements Platform {

    public EternalFireworkRocketForge() {
        EternalFireworkRocket.init(this);
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public void onServerStarted(Consumer<MinecraftServer> callback) {
        MinecraftForge.EVENT_BUS.addListener((ServerStartedEvent event) -> callback.accept(event.getServer()));
    }

    @Override
    public void onServerStopped(Consumer<MinecraftServer> callback) {
        MinecraftForge.EVENT_BUS.addListener((ServerStoppingEvent event) -> callback.accept(event.getServer()));
    }

    @Override
    public void onServerTick(Consumer<MinecraftServer> callback) {
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                callback.accept(event.getServer());
            }
        });
    }

    @Override
    public void onPlayerJoin(Consumer<ServerPlayer> callback) {
        MinecraftForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                callback.accept(player);
            }
        });
    }

    @Override
    public void registerLootTableModifier() {
        MinecraftForge.EVENT_BUS.addListener((LootTableLoadEvent event) -> {
            if (("minecraft:" + ModLootTableModifier.END_CITY_TREASURE).equals(event.getName().toString())) {
                ModLootTableModifier.forEachPool(pool -> event.getTable().addPool(pool.build()));
            }
        });
    }
}
