package xiaoze_qwq_.eternal_firework_rocket.loot;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;
import xiaoze_qwq_.eternal_firework_rocket.util.FireworkData;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
//?} else {
/*import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
*///?}

//? if >=1.21 {
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
//?} else {
/*import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
*///?}

public class ModLootTableModifier {
    public static void registerLootTableModifications() {
        //? if >=1.21 {
        LootTableEvents.MODIFY.register((key, builder, source, lookup) -> {
            if ("minecraft:chests/end_city_treasure".equals(key.location().toString())) {
                addPools(builder);
            }
        });
        //?} else if >=1.20.5 {
        /*LootTableEvents.MODIFY.register((key, builder, source) -> {
            if (key.location().equals(new ResourceLocation("minecraft", "chests/end_city_treasure"))) {
                addPools(builder);
            }
        });
        *///?} else {
        /*LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.equals(new ResourceLocation("minecraft", "chests/end_city_treasure"))) {
                addPools(tableBuilder);
            }
        });
        *///?}
    }

    private static void addPools(LootTable.Builder builder) {
        if (ModConfig.CONFIG.chanceFlight1 > 0) {
            builder.withPool(makePool(1, ModConfig.CONFIG.chanceFlight1));
        }

        if (ModConfig.CONFIG.chanceFlight2 > 0) {
            builder.withPool(makePool(2, ModConfig.CONFIG.chanceFlight2));
        }

        if (ModConfig.CONFIG.chanceFlight3 > 0) {
            builder.withPool(makePool(3, ModConfig.CONFIG.chanceFlight3));
        }
    }

    private static LootPool.Builder makePool(int flight, double chancePercent) {
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
