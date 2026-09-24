package xiaoze_qwq_.eternal_firework_rocket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
            new EternalFireworkRocketItem(itemSettings("eternal_firework_rocket").stacksTo(1));
    public static final Item ULTIMATE_FIREWORK_ROCKET_ITEM =
            new UltimateFireworkRocketItem(itemSettings("eternal_firework_rocket_ultimate").stacksTo(1).fireResistant());

    private static ResourceLocation id(String path) {
        //? if >=1.21 {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
        //?} else {
        /*return new ResourceLocation(MOD_ID, path);
        *///?}
    }

    private static Item.Properties itemSettings(String path) {
        Item.Properties settings = new Item.Properties();
        //? if >=1.21.2 {
        settings.setId(ResourceKey.create(Registries.ITEM, id(path)));
        //?}
        return settings;
    }

    private static EntityType<InvisibleFireworkRocketEntity> registerInvisibleFireworkRocket() {
        ResourceLocation id = id("invisible_firework_rocket");
        EntityType.Builder<InvisibleFireworkRocketEntity> builder = EntityType.Builder
                .<InvisibleFireworkRocketEntity>of(InvisibleFireworkRocketEntity::new, MobCategory.MISC)
                .sized(0.25f, 0.25f)
                .clientTrackingRange(4)
                .updateInterval(10);
        //? if >=1.21.2 {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
        //?} else {
        /*return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build("invisible_firework_rocket"));
        *///?}
    }

    private static EntityType<UltimateFireworkRocketEntity> registerUltimateFireworkRocket() {
        ResourceLocation id = id("ultimate_firework_rocket");
        EntityType.Builder<UltimateFireworkRocketEntity> builder = EntityType.Builder
                .<UltimateFireworkRocketEntity>of(UltimateFireworkRocketEntity::new, MobCategory.MISC)
                .sized(0.25f, 0.25f)
                .clientTrackingRange(4)
                .updateInterval(10);
        //? if >=1.21.2 {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
        //?} else {
        /*return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build("ultimate_firework_rocket"));
        *///?}
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Eternal Firework Rocket Mod Initializing...");

        Registry.register(BuiltInRegistries.ITEM, id("eternal_firework_rocket"), ETERNAL_FIREWORK_ROCKET);
        Registry.register(BuiltInRegistries.ITEM, id("eternal_firework_rocket_ultimate"), ULTIMATE_FIREWORK_ROCKET_ITEM);

        ModConfig.loadConfig();
        ModLootTableModifier.registerLootTableModifications();
        PlayerConversionTracker.registerEvents();

        ServerLifecycleEvents.SERVER_STARTED.register(PlayerConversionTracker::init);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> PlayerConversionTracker.shutdown());

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            PlayerConversionTracker.tick(server);

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                boolean flying = player.isFallFlying();
                if (flying && player.getY() >= EVOLUTION_Y && !PlayerConversionTracker.hasConverted(player)) {
                    InteractionHand hand = findMaxEternalHand(player);
                    if (hand != null) {
                        player.setItemInHand(hand, new ItemStack(ULTIMATE_FIREWORK_ROCKET_ITEM));
                        PlayerConversionTracker.setConverted(player, true);
                        player.displayClientMessage(Component.translatable("message.eternal-firework-rocket.evolution"), true);
                    }
                }
            }
        });

        LOGGER.info("Eternal Firework Rocket Mod Initialized!");
    }

    private static InteractionHand findMaxEternalHand(ServerPlayer player) {
        if (isMaxEternal(player.getMainHandItem())) {
            return InteractionHand.MAIN_HAND;
        }
        if (isMaxEternal(player.getOffHandItem())) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    private static boolean isMaxEternal(ItemStack stack) {
        return stack.is(ETERNAL_FIREWORK_ROCKET) && FireworkData.getFlight(stack) >= FireworkData.MAX_FLIGHT;
    }
}
