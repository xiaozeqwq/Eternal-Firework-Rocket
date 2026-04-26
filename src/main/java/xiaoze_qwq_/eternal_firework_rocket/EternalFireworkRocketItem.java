package xiaoze_qwq_.eternal_firework_rocket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;
import xiaoze_qwq_.eternal_firework_rocket.entity.InvisibleFireworkRocketEntity;

public class EternalFireworkRocketItem extends Item {

    public EternalFireworkRocketItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient && ModConfig.CONFIG.adminOnly && user instanceof ServerPlayerEntity serverPlayer) {
            if (!serverPlayer.hasPermissionLevel(2)) {
                return TypedActionResult.fail(stack);
            }
        }

        if (!user.isFallFlying()) {
            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            int flightDuration;
            if (stack.isOf(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_3)) {
                flightDuration = 3;
            } else if (stack.isOf(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_2)) {
                flightDuration = 2;
            } else {
                flightDuration = 1;
            }

            float cooldownTime = flightDuration - ModConfig.CONFIG.cooldownOffset;
            if (cooldownTime < 0.1f) cooldownTime = 0.1f;
            user.getItemCooldownManager().set(this, (int) (cooldownTime * 20));

            InvisibleFireworkRocketEntity rocket = new InvisibleFireworkRocketEntity(
                    EternalFireworkRocket.INVISIBLE_FIREWORK_ROCKET,
                    world,
                    user,
                    flightDuration * 20
            );
            world.spawnEntity(rocket);

            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 3.0F, 1.0F);

            user.incrementStat(Stats.USED.getOrCreateStat(this));

            // 粒子效果：生成烟花粒子
            for (int i = 0; i < 10; i++) {
                world.addParticle(ParticleTypes.FIREWORK,
                        user.getX() + (world.random.nextDouble() - 0.5) * 0.5,
                        user.getY() + world.random.nextDouble() * 1.0,
                        user.getZ() + (world.random.nextDouble() - 0.5) * 0.5,
                        0, 0, 0);
            }
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}