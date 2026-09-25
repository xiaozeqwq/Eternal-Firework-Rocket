package xiaoze_qwq_.eternal_firework_rocket.loot;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;
import xiaoze_qwq_.eternal_firework_rocket.platform.Platform;
import xiaoze_qwq_.eternal_firework_rocket.util.FireworkData;

import java.util.function.Consumer;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
//?} else {
/*import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
*///?}

public class ModLootTableModifier {

    /** Loot table that receives the firework rocket pools. */
    public static final String END_CITY_TREASURE = "chests/end_city_treasure";

    public static void registerLootTableModifications(Platform platform) {
        platform.registerLootTableModifier();
        //? if fabric {
        FabricLootEvents.register();
        //?}
    }

    /** Adds the configured firework rocket pools to the given loot table. */
    public static void apply(LootTable.Builder builder) {
        forEachPool(builder::withPool);
    }

    /** Passes every configured firework rocket pool to the supplied consumer. */
    public static void forEachPool(Consumer<LootPool.Builder> consumer) {
        if (ModConfig.CONFIG.chanceFlight1 > 0) {
            consumer.accept(makePool(1, ModConfig.CONFIG.chanceFlight1));
        }

        if (ModConfig.CONFIG.chanceFlight2 > 0) {
            consumer.accept(makePool(2, ModConfig.CONFIG.chanceFlight2));
        }

        if (ModConfig.CONFIG.chanceFlight3 > 0) {
            consumer.accept(makePool(3, ModConfig.CONFIG.chanceFlight3));
        }
    }

    public static LootPool.Builder makePool(int flight, double chancePercent) {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET))
                .when(LootItemRandomChanceCondition.randomChance((float) (chancePercent / 100.0)));
        //? if >=1.20.5 {
        return pool.apply(SetComponentsFunction.setComponent(
                DataComponents.FIREWORKS, FireworkData.createComponent(flight)));
        //?} else {
        /*return pool.apply(SetNbtFunction.setTag(FireworkData.createNbt(flight)));
        *///?}
    }
}
