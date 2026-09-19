package xiaoze_qwq_.eternal_firework_rocket.util;

import net.minecraft.item.ItemStack;

//? if >=1.20.5 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import java.util.List;
//?} else {
/*import net.minecraft.nbt.NbtCompound;
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
        FireworksComponent component = stack.get(DataComponentTypes.FIREWORKS);
        return clamp(component != null ? component.flightDuration() : MIN_FLIGHT);
        //?} else {
        /*NbtCompound fireworks = stack.getSubNbt("Fireworks");
        return clamp(fireworks != null ? fireworks.getByte("Flight") : MIN_FLIGHT);
        *///?}
    }

    public static void setFlight(ItemStack stack, int flight) {
        //? if >=1.20.5 {
        stack.set(DataComponentTypes.FIREWORKS, createComponent(flight));
        //?} else {
        /*stack.getOrCreateSubNbt("Fireworks").putByte("Flight", (byte) clamp(flight));
        *///?}
    }

    //? if >=1.20.5 {
    public static FireworksComponent createComponent(int flight) {
        return new FireworksComponent(clamp(flight), List.of());
    }
    //?} else {
    /*public static NbtCompound createNbt(int flight) {
        NbtCompound fireworks = new NbtCompound();
        fireworks.putByte("Flight", (byte) clamp(flight));
        NbtCompound root = new NbtCompound();
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
