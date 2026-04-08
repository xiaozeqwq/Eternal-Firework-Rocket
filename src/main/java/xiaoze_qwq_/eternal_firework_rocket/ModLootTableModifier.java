package xiaoze_qwq_.eternal_firework_rocket.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;

public class ModLootTableModifier {
    public static void registerLootTableModifications() {
        // 监听战利品表修改事件
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            // 检查是否是末地城宝箱
            if (id.equals(new Identifier("minecraft", "chests/end_city_treasure"))) {
                // 添加飞行时间1的烟花火箭 - 将百分比转换为概率
                if (ModConfig.CONFIG.chanceFlight1 > 0) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_1))
                        .conditionally(RandomChanceLootCondition.builder((float)(ModConfig.CONFIG.chanceFlight1 / 100.0)));
                    
                    tableBuilder.pool(poolBuilder.build());
                }
                
                // 添加飞行时间2的烟花火箭
                if (ModConfig.CONFIG.chanceFlight2 > 0) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_2))
                        .conditionally(RandomChanceLootCondition.builder((float)(ModConfig.CONFIG.chanceFlight2 / 100.0)));
                    
                    tableBuilder.pool(poolBuilder.build());
                }
                
                // 添加飞行时间3的烟花火箭
                if (ModConfig.CONFIG.chanceFlight3 > 0) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_3))
                        .conditionally(RandomChanceLootCondition.builder((float)(ModConfig.CONFIG.chanceFlight3 / 100.0)));
                    
                    tableBuilder.pool(poolBuilder.build());
                }
            }
        });
    }
}