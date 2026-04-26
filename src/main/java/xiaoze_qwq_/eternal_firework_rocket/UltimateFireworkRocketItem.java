package xiaoze_qwq_.eternal_firework_rocket.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.entity.UltimateFireworkRocketEntity;

public class UltimateFireworkRocketItem extends Item {
    public UltimateFireworkRocketItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!user.isFallFlying()) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            // 无冷却，直接生成火箭实体
            UltimateFireworkRocketEntity rocket = new UltimateFireworkRocketEntity(
                    EternalFireworkRocket.ULTIMATE_FIREWORK_ROCKET,
                    world,
                    user
            );
            world.spawnEntity(rocket);

            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 3.0F, 1.0F);

            user.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        return TypedActionResult.success(stack, world.isClient());
    }
}