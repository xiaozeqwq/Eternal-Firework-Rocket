package xiaoze_qwq_.eternal_firework_rocket.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;

public class UltimateFireworkRocketEntity extends Entity {
    private LivingEntity shooter;
    private Hand hand;
    private int age = 0;
    private boolean hasAppliedInitialBoost = false;

    // 1.5倍音速 = 510 格/秒 = 25.5 格/tick
    // 原版鞘翅阻力很大，需要极大推力才能维持高速
    private static final double TARGET_SPEED = 510.0;      // 目标速度（格/秒）
    private static final double THRUST_PER_TICK = 12.0;    // 每 tick 推力（大幅提升）
    private static final double PULSE_THRUST = 30.0;       // 短按脉冲推力
    private static final double MAX_SPEED = 600.0;         // 最大速度限制

    public UltimateFireworkRocketEntity(EntityType<?> type, World world) {
        super(type, world);
        this.setInvisible(true);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public UltimateFireworkRocketEntity(EntityType<?> type, World world, LivingEntity shooter, Hand hand) {
        this(type, world);
        this.shooter = shooter;
        this.hand = hand;
        this.setPosition(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    public void tick() {
        super.tick();
        age++;

        if (shooter == null || !shooter.isAlive() || !shooter.isFallFlying()) {
            this.discard();
            return;
        }

        // 检测是否仍在长按右键
        boolean stillUsing = false;
        if (shooter instanceof ServerPlayerEntity serverPlayer) {
            if (serverPlayer.getStackInHand(hand).getItem() == EternalFireworkRocket.ULTIMATE_FIREWORK_ROCKET_ITEM &&
                    serverPlayer.isUsingItem() && serverPlayer.getActiveHand() == hand) {
                stillUsing = true;
            }
        }

        if (!stillUsing) {
            // 短按：未长按且年龄很小（刚使用就松开）
            if (!hasAppliedInitialBoost && age < 10) {
                Vec3d direction = shooter.getRotationVector();
                shooter.addVelocity(direction.x * PULSE_THRUST, direction.y * PULSE_THRUST, direction.z * PULSE_THRUST);
                shooter.velocityModified = true;
                if (!shooter.getWorld().isClient) {
                    shooter.getWorld().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                            net.minecraft.sound.SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST,
                            net.minecraft.sound.SoundCategory.PLAYERS, 2.0F, 0.8F);
                }
                hasAppliedInitialBoost = true;
            }
            this.discard();
            return;
        }

        // 长按：持续加速
        if (!this.getWorld().isClient) {
            Vec3d direction = shooter.getRotationVector();
            Vec3d currentVel = shooter.getVelocity();
            double currentSpeed = currentVel.length();

            // 始终添加推力（克服阻力）
            shooter.addVelocity(direction.x * THRUST_PER_TICK, direction.y * THRUST_PER_TICK, direction.z * THRUST_PER_TICK);
            shooter.velocityModified = true;

            // 速度上限限制
            Vec3d newVel = shooter.getVelocity();
            if (newVel.length() > MAX_SPEED) {
                newVel = newVel.normalize().multiply(MAX_SPEED);
                shooter.setVelocity(newVel);
                shooter.velocityModified = true;
            }
        }

        hasAppliedInitialBoost = true;
        this.setPosition(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}
    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) { return false; }
    @Override
    public boolean shouldSave() { return false; }
}