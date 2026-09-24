package xiaoze_qwq_.eternal_firework_rocket.util;

import net.minecraft.world.item.ItemStack;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Fireworks;
import java.util.List;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

/**
 * Reads and writes the flight value using vanilla firework data
 * ({@code Fireworks.Flight}, range 1-3). On 1.20.5+ this is the
 * {@code minecraft:fireworks} component, below that it is the raw NBT tag.
 */
public final class FireworkData {
    public static final int MIN_FLIGHT = 1;
    public static final int MAX_FLIGHT = 3;

    private FireworkData() {}

    public static int getFlight(ItemStack stack) {
        //? if >=1.20.5 {
        Fireworks component = stack.get(DataComponents.FIREWORKS);
        return clamp(component != null ? component.flightDuration() : MIN_FLIGHT);
        //?} else {
        /*CompoundTag fireworks = stack.getTagElement("Fireworks");
        return clamp(fireworks != null ? fireworks.getByte("Flight") : MIN_FLIGHT);
        *///?}
    }

    public static void setFlight(ItemStack stack, int flight) {
        //? if >=1.20.5 {
        stack.set(DataComponents.FIREWORKS, createComponent(flight));
        //?} else {
        /*stack.getOrCreateTagElement("Fireworks").putByte("Flight", (byte) clamp(flight));
        *///?}
    }

    //? if >=1.20.5 {
    public static Fireworks createComponent(int flight) {
        return new Fireworks(clamp(flight), List.of());
    }
    //?} else {
    /*public static CompoundTag createNbt(int flight) {
        CompoundTag fireworks = new CompoundTag();
        fireworks.putByte("Flight", (byte) clamp(flight));
        CompoundTag root = new CompoundTag();
        root.put("Fireworks", fireworks);
        return root;
    }
    *///?}

    public static int clamp(int flight) {
        if (flight < MIN_FLIGHT) {
            return MIN_FLIGHT;
        }
        if (flight > MAX_FLIGHT) {
            return MAX_FLIGHT;
        }
        return flight;
    }
}
