package xiaoze_qwq_.eternal_firework_rocket.loot;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;

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
            builder.pool(makePool(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_1, ModConfig.CONFIG.chanceFlight1));
        }

        if (ModConfig.CONFIG.chanceFlight2 > 0) {
            builder.pool(makePool(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_2, ModConfig.CONFIG.chanceFlight2));
        }

        if (ModConfig.CONFIG.chanceFlight3 > 0) {
            builder.pool(makePool(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_3, ModConfig.CONFIG.chanceFlight3));
        }
    }

    private static LootPool.Builder makePool(net.minecraft.item.Item item, double chancePercent) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(item))
                .conditionally(RandomChanceLootCondition.builder((float) (chancePercent / 100.0)));
    }
}
