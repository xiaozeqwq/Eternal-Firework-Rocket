package xiaoze_qwq_.eternal_firework_rocket.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class UltimateFireworkRocketEntity extends Entity {
    private LivingEntity shooter;
    private int life;
    private final int lifeTime = 100;  // 5秒

    // 推力参数：达到 25.5 格/tick (510格/秒) 的目标速度
    // 公式 v_new = 0.5*v + d*u，d = 目标速度 * (1-0.5) = 12.75
    private static final double DAMPING = 0.5;           // 阻尼系数
    private static final double FORCE = 12.75;           // 推力系数（u方向直接增加的量）

    public UltimateFireworkRocketEntity(EntityType<?> type, World world) {
        super(type, world);
        this.setInvisible(true);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public UltimateFireworkRocketEntity(EntityType<?> type, World world, LivingEntity shooter) {
        this(type, world);
        this.shooter = shooter;
        this.setPosition(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        if (shooter == null || !shooter.isAlive() || !shooter.isFallFlying()) {
            this.discard();
            return;
        }

        life++;
        if (life > lifeTime) {
            this.discard();
            return;
        }

        // 递推动量更新（与原版烟花火箭一致，但强很多）
        Vec3d vel = shooter.getVelocity();
        Vec3d look = shooter.getRotationVector();
        double newVelX = vel.x * DAMPING + look.x * FORCE;
        double newVelY = vel.y * DAMPING + look.y * FORCE;
        double newVelZ = vel.z * DAMPING + look.z * FORCE;
        shooter.setVelocity(newVelX, newVelY, newVelZ);
        shooter.velocityModified = true;

        // 粒子效果（客户端）
        if (getWorld().isClient) {
            Vec3d pos = shooter.getPos();
            for (int i = 0; i < 5; i++) {
                getWorld().addParticle(ParticleTypes.FLAME,
                        pos.x + (random.nextDouble() - 0.5) * 1.0,
                        pos.y + random.nextDouble() * 1.5,
                        pos.z + (random.nextDouble() - 0.5) * 1.0,
                        0, 0, 0);
                getWorld().addParticle(ParticleTypes.LARGE_SMOKE,
                        pos.x + (random.nextDouble() - 0.5) * 1.0,
                        pos.y + random.nextDouble() * 1.5,
                        pos.z + (random.nextDouble() - 0.5) * 1.0,
                        0, 0, 0);
            }
        }

        // 跟随玩家手持位置（视觉占位）
        Vec3d handOffset = shooter.getHandPosOffset(net.minecraft.item.Items.FIREWORK_ROCKET);
        this.setPosition(shooter.getX() + handOffset.x, shooter.getY() + handOffset.y, shooter.getZ() + handOffset.z);
        this.setVelocity(shooter.getVelocity());
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