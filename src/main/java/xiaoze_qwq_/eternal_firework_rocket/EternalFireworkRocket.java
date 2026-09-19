package xiaoze_qwq_.eternal_firework_rocket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;
import xiaoze_qwq_.eternal_firework_rocket.entity.InvisibleFireworkRocketEntity;
import xiaoze_qwq_.eternal_firework_rocket.entity.UltimateFireworkRocketEntity;
import xiaoze_qwq_.eternal_firework_rocket.item.EternalFireworkRocketItem;
import xiaoze_qwq_.eternal_firework_rocket.item.UltimateFireworkRocketItem;
import xiaoze_qwq_.eternal_firework_rocket.loot.ModLootTableModifier;
import xiaoze_qwq_.eternal_firework_rocket.util.FireworkData;
import xiaoze_qwq_.eternal_firework_rocket.util.PlayerConversionTracker;

//? if >=1.21.2 {
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
//?}

public class EternalFireworkRocket implements ModInitializer {
    public static final String MOD_ID = "eternal-firework-rocket";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final double EVOLUTION_Y = 114514.0;

    public static final EntityType<InvisibleFireworkRocketEntity> INVISIBLE_FIREWORK_ROCKET =
            registerInvisibleFireworkRocket();

    public static final EntityType<UltimateFireworkRocketEntity> ULTIMATE_FIREWORK_ROCKET =
            registerUltimateFireworkRocket();

    public static final Item ETERNAL_FIREWORK_ROCKET =
            new EternalFireworkRocketItem(itemSettings("eternal_firework_rocket").maxCount(1));
    public static final Item ULTIMATE_FIREWORK_ROCKET_ITEM =
            new UltimateFireworkRocketItem(itemSettings("eternal_firework_rocket_ultimate").maxCount(1).fireproof());

    private static Identifier id(String path) {
        //? if >=1.21 {
        return Identifier.of(MOD_ID, path);
        //?} else {
        /*return new Identifier(MOD_ID, path);
        *///?}
    }

    private static Item.Settings itemSettings(String path) {
        Item.Settings settings = new Item.Settings();
        //? if >=1.21.2 {
        settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, id(path)));
        //?}
        return settings;
    }

    private static EntityType<InvisibleFireworkRocketEntity> registerInvisibleFireworkRocket() {
        Identifier id = id("invisible_firework_rocket");
        EntityType.Builder<InvisibleFireworkRocketEntity> builder = EntityType.Builder
                .<InvisibleFireworkRocketEntity>create(InvisibleFireworkRocketEntity::new, SpawnGroup.MISC)
                //? if >=1.20.5 {
                .dimensions(0.25f, 0.25f)
                //?} else {
                /*.setDimensions(0.25f, 0.25f)
                *///?}
                .maxTrackingRange(4)
                .trackingTickInterval(10);
        //? if >=1.21.2 {
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);
        return Registry.register(Registries.ENTITY_TYPE, key, builder.build(key));
        //?} else {
        /*return Registry.register(Registries.ENTITY_TYPE, id, builder.build("invisible_firework_rocket"));
        *///?}
    }

    private static EntityType<UltimateFireworkRocketEntity> registerUltimateFireworkRocket() {
        Identifier id = id("ultimate_firework_rocket");
        EntityType.Builder<UltimateFireworkRocketEntity> builder = EntityType.Builder
                .<UltimateFireworkRocketEntity>create(UltimateFireworkRocketEntity::new, SpawnGroup.MISC)
                //? if >=1.20.5 {
                .dimensions(0.25f, 0.25f)
                //?} else {
                /*.setDimensions(0.25f, 0.25f)
                *///?}
                .maxTrackingRange(4)
                .trackingTickInterval(10);
        //? if >=1.21.2 {
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);
        return Registry.register(Registries.ENTITY_TYPE, key, builder.build(key));
        //?} else {
        /*return Registry.register(Registries.ENTITY_TYPE, id, builder.build("ultimate_firework_rocket"));
        *///?}
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Eternal Firework Rocket Mod Initializing...");

        Registry.register(Registries.ITEM, id("eternal_firework_rocket"), ETERNAL_FIREWORK_ROCKET);
        Registry.register(Registries.ITEM, id("eternal_firework_rocket_ultimate"), ULTIMATE_FIREWORK_ROCKET_ITEM);

        ModConfig.loadConfig();
        ModLootTableModifier.registerLootTableModifications();
        PlayerConversionTracker.registerEvents();

        ServerLifecycleEvents.SERVER_STARTED.register(PlayerConversionTracker::init);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> PlayerConversionTracker.shutdown());

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            PlayerConversionTracker.tick(server);

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                //? if >=1.21.2 {
                boolean flying = player.isGliding();
                //?} else {
                /*boolean flying = player.isFallFlying();
                *///?}
                if (flying && player.getY() >= EVOLUTION_Y && !PlayerConversionTracker.hasConverted(player)) {
                    Hand hand = findMaxEternalHand(player);
                    if (hand != null) {
                        player.setStackInHand(hand, new ItemStack(ULTIMATE_FIREWORK_ROCKET_ITEM));
                        PlayerConversionTracker.setConverted(player, true);
                        player.sendMessage(Text.translatable("message.eternal-firework-rocket.evolution"), true);
                    }
                }
            }
        });

        LOGGER.info("Eternal Firework Rocket Mod Initialized!");
    }

    private static Hand findMaxEternalHand(ServerPlayerEntity player) {
        if (isMaxEternal(player.getMainHandStack())) {
            return Hand.MAIN_HAND;
        }
        if (isMaxEternal(player.getOffHandStack())) {
            return Hand.OFF_HAND;
        }
        return null;
    }

    private static boolean isMaxEternal(ItemStack stack) {
        return stack.isOf(ETERNAL_FIREWORK_ROCKET) && FireworkData.getFlight(stack) >= FireworkData.MAX_FLIGHT;
    }
}
