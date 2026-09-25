package xiaoze_qwq_.eternal_firework_rocket.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.loot.ModLootTableModifier;
import xiaoze_qwq_.eternal_firework_rocket.platform.Platform;

import java.nio.file.Path;
import java.util.function.Consumer;

//? if >=1.21 {
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
//?} else {
/*import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
*///?}

public class EternalFireworkRocketFabric implements ModInitializer, Platform {

    @Override
    public void onInitialize() {
        EternalFireworkRocket.init(this);
    }

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public void onServerStarted(Consumer<MinecraftServer> callback) {
        ServerLifecycleEvents.SERVER_STARTED.register(callback::accept);
    }

    @Override
    public void onServerStopped(Consumer<MinecraftServer> callback) {
        ServerLifecycleEvents.SERVER_STOPPED.register(callback::accept);
    }

    @Override
    public void onServerTick(Consumer<MinecraftServer> callback) {
        ServerTickEvents.END_SERVER_TICK.register(callback::accept);
    }

    @Override
    public void onPlayerJoin(Consumer<ServerPlayer> callback) {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> callback.accept(handler.getPlayer()));
    }

    @Override
    public void registerLootTableModifier() {
        //? if >=1.21 {
        LootTableEvents.MODIFY.register((key, builder, source, lookup) -> {
            if (("minecraft:" + ModLootTableModifier.END_CITY_TREASURE).equals(key.location().toString())) {
                ModLootTableModifier.apply(builder);
            }
        });
        //?} else {
        /*LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (("minecraft:" + ModLootTableModifier.END_CITY_TREASURE).equals(id.toString())) {
                ModLootTableModifier.apply(tableBuilder);
            }
        });
        *///?}
    }
}
