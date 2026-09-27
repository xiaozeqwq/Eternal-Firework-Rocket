//? if forge {
package xiaoze_qwq_.eternal_firework_rocket.loot;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Forge-specific loot table hook. Kept in the shared source tree so that the
 * Stonecutter version conditionals are processed.
 *
 * <p>Forge only added {@code LootTable#addPool} in Minecraft 1.20.1. On 1.20 the
 * (mutable) {@code pools} list is used directly through reflection instead, trying
 * both the development (official) and production (SRG) field names.</p>
 */
public final class ForgeLootEvents {
    private ForgeLootEvents() {}

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener((LootTableLoadEvent event) -> {
            if (("minecraft:" + ModLootTableModifier.END_CITY_TREASURE).equals(event.getName().toString())) {
                //? if >=1.20.1 {
                ModLootTableModifier.forEachPool(pool -> event.getTable().addPool(pool.build()));
                //?} else {
                /*ModLootTableModifier.forEachPool(pool -> addPool(event.getTable(), pool.build()));
                *///?}
            }
        });
    }

    //? if <1.20.1 {
    /*private static void addPool(LootTable table, LootPool pool) {
        try {
            Field poolsField = findPoolsField();
            poolsField.setAccessible(true);
            List<LootPool> pools = (List<LootPool>) poolsField.get(table);
            pools.add(pool);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to add a loot pool to " + table, exception);
        }
    }

    private static Field findPoolsField() throws NoSuchFieldException {
        for (String name : new String[] { "pools", "f_79109_" }) {
            try {
                return LootTable.class.getDeclaredField(name);
            } catch (NoSuchFieldException exception) {
                // Try the next name.
            }
        }
        throw new NoSuchFieldException("pools");
    }
    *///?}
}
//?}
