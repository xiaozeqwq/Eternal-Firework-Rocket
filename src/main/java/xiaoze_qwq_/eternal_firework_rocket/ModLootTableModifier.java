package xiaoze_qwq_.eternal_firework_rocket.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;

public class ModLootTableModifier {
    public static void registerLootTableModifications() {
        LootTableEvents.MODIFY.register(new LootTableEvents.Modify() {
            @Override
            public void modifyLootTable(RegistryKey<LootTable> key,
                                        LootTable.Builder builder,
                                        LootTableSource source) {
                if (key.getValue().equals(Identifier.of("minecraft", "chests/end_city_treasure"))) {
                    
                    if (ModConfig.CONFIG.chanceFlight1 > 0) {
                        builder.pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .with(ItemEntry.builder(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_1))
                                .conditionally(RandomChanceLootCondition.builder((float) (ModConfig.CONFIG.chanceFlight1 / 100.0)))
                                .build());
                    }

                    if (ModConfig.CONFIG.chanceFlight2 > 0) {
                        builder.pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .with(ItemEntry.builder(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_2))
                                .conditionally(RandomChanceLootCondition.builder((float) (ModConfig.CONFIG.chanceFlight2 / 100.0)))
                                .build());
                    }

                    if (ModConfig.CONFIG.chanceFlight3 > 0) {
                        builder.pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .with(ItemEntry.builder(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_3))
                                .conditionally(RandomChanceLootCondition.builder((float) (ModConfig.CONFIG.chanceFlight3 / 100.0)))
                                .build());
                    }
                }
            }
        });
    }
}