package xiaoze_qwq_.eternal_firework_rocket.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class UltimateFireworkRocketEntity extends Entity {
    private LivingEntity shooter;
    private int life;          // 已存在 tick 数
    private final int lifeTime = 100;  // 5 秒 = 100 ticks

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

        // 模仿普通火箭推力，但更强：基础加速系数放大 2.5 倍
        Vec3d rotation = shooter.getRotationVector();
        Vec3d velocity = shooter.getVelocity();
        shooter.setVelocity(
            velocity.add(
                rotation.x * 0.25 + (rotation.x * 3.75 - velocity.x) * 0.5,
                rotation.y * 0.25 + (rotation.y * 3.75 - velocity.y) * 0.5,
                rotation.z * 0.25 + (rotation.z * 3.75 - velocity.z) * 0.5
            )
        );
        shooter.velocityModified = true;

        // 跟随玩家手持位置（只是视觉效果，实际不可见）
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