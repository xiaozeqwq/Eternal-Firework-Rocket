package xiaoze_qwq_.eternal_firework_rocket.loot;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;
import xiaoze_qwq_.eternal_firework_rocket.util.FireworkData;

//? if >=1.20.5 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.loot.function.SetComponentsLootFunction;
//?} else {
/*import net.minecraft.loot.function.SetNbtLootFunction;
*///?}

//? if >=1.21 {
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
//?} else {
/*import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.util.Identifier;
*///?}

public class ModLootTableModifier {
    public static void registerLootTableModifications() {
        //? if >=1.21 {
        LootTableEvents.MODIFY.register((key, builder, source, lookup) -> {
            if ("minecraft:chests/end_city_treasure".equals(key.getValue().toString())) {
                addPools(builder);
            }
        });
        //?} else if >=1.20.5 {
        /*LootTableEvents.MODIFY.register((key, builder, source) -> {
            if (key.getValue().equals(new Identifier("minecraft", "chests/end_city_treasure"))) {
                addPools(builder);
            }
        });
        *///?} else {
        /*LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.equals(new Identifier("minecraft", "chests/end_city_treasure"))) {
                addPools(tableBuilder);
            }
        });
        *///?}
    }

    private static void addPools(LootTable.Builder builder) {
        if (ModConfig.CONFIG.chanceFlight1 > 0) {
            builder.pool(makePool(1, ModConfig.CONFIG.chanceFlight1));
        }

        if (ModConfig.CONFIG.chanceFlight2 > 0) {
            builder.pool(makePool(2, ModConfig.CONFIG.chanceFlight2));
        }

        if (ModConfig.CONFIG.chanceFlight3 > 0) {
            builder.pool(makePool(3, ModConfig.CONFIG.chanceFlight3));
        }
    }

    private static LootPool.Builder makePool(int flight, double chancePercent) {
        LootPool.Builder pool = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET))
                .conditionally(RandomChanceLootCondition.builder((float) (chancePercent / 100.0)));
        //? if >=1.20.5 {
        return pool.apply(SetComponentsLootFunction.builder(
                DataComponentTypes.FIREWORKS, FireworkData.createComponent(flight)));
        //?} else {
        /*return pool.apply(SetNbtLootFunction.builder(FireworkData.createNbt(flight)));
        *///?}
    }
}
