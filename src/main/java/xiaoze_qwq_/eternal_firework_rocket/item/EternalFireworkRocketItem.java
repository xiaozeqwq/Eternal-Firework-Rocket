package xiaoze_qwq_.eternal_firework_rocket.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;
import xiaoze_qwq_.eternal_firework_rocket.entity.InvisibleFireworkRocketEntity;
import xiaoze_qwq_.eternal_firework_rocket.util.FireworkData;
import xiaoze_qwq_.eternal_firework_rocket.util.PermissionHelper;

//? if >=1.21.2 {
import net.minecraft.world.InteractionResult;
//?} else {
/*import net.minecraft.world.InteractionResultHolder;
*///?}

public class EternalFireworkRocketItem extends Item {

    public EternalFireworkRocketItem(Properties settings) {
        super(settings);
    }

    @Override
    //? if >=1.21.2 {
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
    //?} else {
    /*public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
    *///?}
        ItemStack stack = user.getItemInHand(hand);

        if (!world.isClientSide() && !PermissionHelper.hasUsePermission(user)) {
            //? if >=1.21.2 {
            return InteractionResult.FAIL;
            //?} else {
            /*return InteractionResultHolder.fail(stack);
            *///?}
        }

        if (!user.isFallFlying()) {
            //? if >=1.21.2 {
            return InteractionResult.FAIL;
            //?} else {
            /*return InteractionResultHolder.fail(stack);
            *///?}
        }

        if (!world.isClientSide()) {
            int flightDuration = FireworkData.getFlight(stack);

            float cooldownTime = flightDuration - ModConfig.CONFIG.cooldownOffset;
            if (cooldownTime < 0.1f) cooldownTime = 0.1f;
            //? if >=1.21.2 {
            user.getCooldowns().addCooldown(stack, (int) (cooldownTime * 20));
            //?} else {
            /*user.getCooldowns().addCooldown(this, (int) (cooldownTime * 20));
            *///?}

            InvisibleFireworkRocketEntity rocket = new InvisibleFireworkRocketEntity(
                    EternalFireworkRocket.INVISIBLE_FIREWORK_ROCKET,
                    world,
                    user,
                    flightDuration * 20
            );
            world.addFreshEntity(rocket);

            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0F, 1.0F);

            user.awardStat(Stats.ITEM_USED.get(this));

            for (int i = 0; i < 10; i++) {
                world.addParticle(ParticleTypes.FIREWORK,
                        user.getX() + (world.random.nextDouble() - 0.5) * 0.5,
                        user.getY() + world.random.nextDouble() * 1.0,
                        user.getZ() + (world.random.nextDouble() - 0.5) * 0.5,
                        0, 0, 0);
            }
        }

        //? if >=1.21.2 {
        return InteractionResult.SUCCESS;
        //?} else {
        /*return InteractionResultHolder.success(stack, world.isClientSide());
        *///?}
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}
