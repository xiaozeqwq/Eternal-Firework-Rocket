package xiaoze_qwq_.eternal_firework_rocket;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;
import xiaoze_qwq_.eternal_firework_rocket.loot.ModLootTableModifier;

public class EternalFireworkRocket implements ModInitializer {
    public static final String MOD_ID = "eternal-firework-rocket";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // 注册三种飞行时间的永恒烟花火箭
    public static final Item ETERNAL_FIREWORK_ROCKET_1 = new EternalFireworkRocketItem(new Item.Settings().maxCount(1));
    public static final Item ETERNAL_FIREWORK_ROCKET_2 = new EternalFireworkRocketItem(new Item.Settings().maxCount(1));
    public static final Item ETERNAL_FIREWORK_ROCKET_3 = new EternalFireworkRocketItem(new Item.Settings().maxCount(1));

    @Override
    public void onInitialize() {
        LOGGER.info("Eternal Firework Rocket Mod Initializing...");
        
        // 注册物品
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_1"), ETERNAL_FIREWORK_ROCKET_1);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_2"), ETERNAL_FIREWORK_ROCKET_2);
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "eternal_firework_rocket_3"), ETERNAL_FIREWORK_ROCKET_3);
        
        // 加载配置
        ModConfig.loadConfig();
        
        // 注册战利品表修改
        ModLootTableModifier.registerLootTableModifications();
        
        LOGGER.info("Eternal Firework Rocket Mod Initialized!");
    }
}