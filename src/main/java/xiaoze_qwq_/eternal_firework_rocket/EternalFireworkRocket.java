package xiaoze_qwq_.eternal_firework_rocket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;
import xiaoze_qwq_.eternal_firework_rocket.entity.InvisibleFireworkRocketEntity;
import xiaoze_qwq_.eternal_firework_rocket.entity.UltimateFireworkRocketEntity;
import xiaoze_qwq_.eternal_firework_rocket.item.EternalFireworkRocketItem;
import xiaoze_qwq_.eternal_firework_rocket.item.UltimateFireworkRocketItem;
import xiaoze_qwq_.eternal_firework_rocket.loot.ModLootTableModifier;
import xiaoze_qwq_.eternal_firework_rocket.util.PlayerConversionTracker;

//? if >=1.21.2 {
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
//?}

public class EternalFireworkRocket implements ModInitializer {
    public static final String MOD_ID = "eternal-firework-rocket";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final EntityType<InvisibleFireworkRocketEntity> INVISIBLE_FIREWORK_ROCKET =
            registerInvisibleFireworkRocket();

    public static final EntityType<UltimateFireworkRocketEntity> ULTIMATE_FIREWORK_ROCKET =
            registerUltimateFireworkRocket();

    public static final Item ETERNAL_FIREWORK_ROCKET_1 = new EternalFireworkRocketItem(itemSettings("eternal_firework_rocket_1").maxCount(1));
    public static final Item ETERNAL_FIREWORK_ROCKET_2 = new EternalFireworkRocketItem(itemSettings("eternal_firework_rocket_2").maxCount(1));
    public static final Item ETERNAL_FIREWORK_ROCKET_3 = new EternalFireworkRocketItem(itemSettings("eternal_firework_rocket_3").maxCount(1));
    public static final Item ULTIMATE_FIREWORK_ROCKET_ITEM = new UltimateFireworkRocketItem(itemSettings("eternal_firework_rocket_ultimate").maxCount(1).fireproof());

    private static Item.Settings itemSettings(String path) {
        Item.Settings settings = new Item.Settings();
        //? if >=1.21.2 {
        settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, path)));
        //?}
        return settings;
    }

    private static EntityType<InvisibleFireworkRocketEntity> registerInvisibleFireworkRocket() {
        Identifier id = Identifier.of(MOD_ID, "invisible_firework_rocket");
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
        Identifier id = Identifier.of(MOD_ID, "ultimate_firework_rocket");
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

        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_1"), ETERNAL_FIREWORK_ROCKET_1);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_2"), ETERNAL_FIREWORK_ROCKET_2);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_3"), ETERNAL_FIREWORK_ROCKET_3);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_ultimate"), ULTIMATE_FIREWORK_ROCKET_ITEM);

        ModConfig.loadConfig();
        ModLootTableModifier.registerLootTableModifications();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            PlayerConversionTracker.init(server);

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                //? if >=1.21.2 {
                boolean flying = player.isGliding();
                //?} else {
                /*boolean flying = player.isFallFlying();
                *///?}
                if (flying) {
                    ItemStack stack = player.getMainHandStack();
                    if (stack.getItem() == ETERNAL_FIREWORK_ROCKET_3 && player.getY() >= 114514.0) {
                        if (!PlayerConversionTracker.hasConverted(player)) {
                            ItemStack newStack = new ItemStack(ULTIMATE_FIREWORK_ROCKET_ITEM);
                            //? if >=1.21.5 {
                            player.getInventory().setStack(player.getInventory().getSelectedSlot(), newStack);
                            //?} else {
                            /*player.getInventory().setStack(player.getInventory().selectedSlot, newStack);
                            *///?}
                            PlayerConversionTracker.setConverted(player, true);
                            player.sendMessage(Text.translatable("message.eternal-firework-rocket.evolution"), true);
                        }
                    }
                }
            }
        });

        LOGGER.info("Eternal Firework Rocket Mod Initialized!");
    }
}
