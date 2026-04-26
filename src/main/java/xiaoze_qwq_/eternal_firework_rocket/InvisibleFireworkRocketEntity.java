package xiaoze_qwq_.eternal_firework_rocket.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class InvisibleFireworkRocketEntity extends Entity {
    private LivingEntity shooter;
    private int life;
    private int lifeTime;

    public InvisibleFireworkRocketEntity(EntityType<?> type, World world) {
        super(type, world);
        this.setInvisible(true);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public InvisibleFireworkRocketEntity(EntityType<?> type, World world, LivingEntity shooter, int lifetime) {
        this(type, world);
        this.shooter = shooter;
        this.lifeTime = lifetime;
        this.setPosition(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        if (shooter == null || !shooter.isAlive()) {
            this.discard();
            return;
        }

        if (!shooter.isFallFlying()) {
            this.discard();
            return;
        }

        life++;

        Vec3d rotation = shooter.getRotationVector();
        Vec3d velocity = shooter.getVelocity();
        shooter.setVelocity(
            velocity.add(
                rotation.x * 0.1 + (rotation.x * 1.5 - velocity.x) * 0.5,
                rotation.y * 0.1 + (rotation.y * 1.5 - velocity.y) * 0.5,
                rotation.z * 0.1 + (rotation.z * 1.5 - velocity.z) * 0.5
            )
        );
        shooter.velocityModified = true;

        Vec3d handOffset = shooter.getHandPosOffset(Items.FIREWORK_ROCKET);
        this.setPosition(shooter.getX() + handOffset.x, shooter.getY() + handOffset.y, shooter.getZ() + handOffset.z);
        this.setVelocity(shooter.getVelocity());

        if (life > lifeTime) {
            this.discard();
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return false;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}