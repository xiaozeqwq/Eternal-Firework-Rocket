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
import xiaoze_qwq_.eternal_firework_rocket.item.UltimateFireworkRocketItem;
import xiaoze_qwq_.eternal_firework_rocket.loot.ModLootTableModifier;
import xiaoze_qwq_.eternal_firework_rocket.util.PlayerConversionTracker;

public class EternalFireworkRocket implements ModInitializer {
    public static final String MOD_ID = "eternal-firework-rocket";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // 实体
    public static final EntityType<InvisibleFireworkRocketEntity> INVISIBLE_FIREWORK_ROCKET =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of(MOD_ID, "invisible_firework_rocket"),
                    EntityType.Builder.<InvisibleFireworkRocketEntity>create(
                                    InvisibleFireworkRocketEntity::new, SpawnGroup.MISC)
                            .dimensions(0.25f, 0.25f)
                            .maxTrackingRange(4)
                            .trackingTickInterval(10)
                            .build()
            );

    public static final EntityType<UltimateFireworkRocketEntity> ULTIMATE_FIREWORK_ROCKET =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of(MOD_ID, "ultimate_firework_rocket"),
                    EntityType.Builder.<UltimateFireworkRocketEntity>create(
                                    UltimateFireworkRocketEntity::new, SpawnGroup.MISC)
                            .dimensions(0.25f, 0.25f)
                            .maxTrackingRange(4)
                            .trackingTickInterval(10)
                            .build()
            );

    // 物品
    public static final Item ETERNAL_FIREWORK_ROCKET_1 = new EternalFireworkRocketItem(new Item.Settings().maxCount(1));
    public static final Item ETERNAL_FIREWORK_ROCKET_2 = new EternalFireworkRocketItem(new Item.Settings().maxCount(1));
    public static final Item ETERNAL_FIREWORK_ROCKET_3 = new EternalFireworkRocketItem(new Item.Settings().maxCount(1));
    public static final Item ULTIMATE_FIREWORK_ROCKET_ITEM = new UltimateFireworkRocketItem(new Item.Settings().maxCount(1).fireproof());

    @Override
    public void onInitialize() {
        LOGGER.info("Eternal Firework Rocket Mod Initializing...");

        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_1"), ETERNAL_FIREWORK_ROCKET_1);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_2"), ETERNAL_FIREWORK_ROCKET_2);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_3"), ETERNAL_FIREWORK_ROCKET_3);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_ultimate"), ULTIMATE_FIREWORK_ROCKET_ITEM);

        ModConfig.loadConfig();
        ModLootTableModifier.registerLootTableModifications();

        // 三级火箭 → 终极火箭 转换（每人仅一次）
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // 确保持久化目录已初始化（使用当前 server 的世界路径）
            PlayerConversionTracker.init(server);

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.isFallFlying()) {
                    ItemStack stack = player.getMainHandStack();
                    if (stack.getItem() == ETERNAL_FIREWORK_ROCKET_3 && player.getY() >= 114514.0) {
                        // 检查是否已经转化过
                        if (!PlayerConversionTracker.hasConverted(player)) {
                            // 替换为终极火箭
                            ItemStack newStack = new ItemStack(ULTIMATE_FIREWORK_ROCKET_ITEM);
                            player.getInventory().setStack(player.getInventory().selectedSlot, newStack);
                            // 标记已转化
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