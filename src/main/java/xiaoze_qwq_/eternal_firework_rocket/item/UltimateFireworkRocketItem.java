package xiaoze_qwq_.eternal_firework_rocket.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.entity.UltimateFireworkRocketEntity;
import xiaoze_qwq_.eternal_firework_rocket.util.PermissionHelper;

//? if >=1.21.2 {
import net.minecraft.world.InteractionResult;
//?} else {
/*import net.minecraft.world.InteractionResultHolder;
*///?}

public class UltimateFireworkRocketItem extends Item {
    public UltimateFireworkRocketItem(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
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
            UltimateFireworkRocketEntity rocket = new UltimateFireworkRocketEntity(
                    EternalFireworkRocket.ULTIMATE_FIREWORK_ROCKET,
                    world,
                    user
            );
            world.addFreshEntity(rocket);

            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0F, 1.0F);

            user.awardStat(Stats.ITEM_USED.get(this));
        }

        //? if >=1.21.2 {
        return InteractionResult.SUCCESS;
        //?} else {
        /*return InteractionResultHolder.success(stack, world.isClientSide());
        *///?}
    }
}
