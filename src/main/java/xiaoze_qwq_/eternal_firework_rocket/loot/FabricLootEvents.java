//? if fabric {
package xiaoze_qwq_.eternal_firework_rocket.loot;

//? if >=1.21 {
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
//?} else if >=1.20.5 {
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
//?} else {
/*import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
*///?}

/**
 * Fabric-specific loot table hook. Kept in the shared source tree so that the
 * Stonecutter version conditionals are processed.
 */
public final class FabricLootEvents {
    private FabricLootEvents() {}

    public static void register() {
        //? if >=1.21 {
        LootTableEvents.MODIFY.register((key, builder, source, lookup) -> {
            if (("minecraft:" + ModLootTableModifier.END_CITY_TREASURE).equals(key.location().toString())) {
                ModLootTableModifier.apply(builder);
            }
        });
        //?} else if >=1.20.5 {
        LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
            if (("minecraft:" + ModLootTableModifier.END_CITY_TREASURE).equals(key.location().toString())) {
                ModLootTableModifier.apply(tableBuilder);
            }
        });
        //?} else {
        /*LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (("minecraft:" + ModLootTableModifier.END_CITY_TREASURE).equals(id.toString())) {
                ModLootTableModifier.apply(tableBuilder);
            }
        });
        *///?}
    }
}
//?}
