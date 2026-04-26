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

    private static final double TARGET_SPEED = 510.0;  // 格/秒
    private static final double SPEED_PER_TICK = TARGET_SPEED / 20.0;  // 25.5 格/tick

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

        // 设置速度为目标方向 * 超音速
        Vec3d lookVec = shooter.getRotationVector();
        Vec3d targetVelocity = lookVec.multiply(SPEED_PER_TICK);
        shooter.setVelocity(targetVelocity);
        shooter.velocityModified = true;

        // 粒子效果（火焰 + 烟雾）
        if (getWorld().isClient) {
            // 客户端生成粒子
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

        // 跟随玩家手持位置（实体不可见，仅用于位置同步）
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