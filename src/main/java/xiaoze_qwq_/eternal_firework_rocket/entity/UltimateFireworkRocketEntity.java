package xiaoze_qwq_.eternal_firework_rocket.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

//? if >=1.21.6 {
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
//?} else {
/*import net.minecraft.nbt.NbtCompound;
*///?}

public class UltimateFireworkRocketEntity extends Entity {
    private LivingEntity shooter;
    private int life;
    private final int lifeTime = 100;

    private static final double DAMPING = 0.5;
    private static final double FORCE = 12.75;

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

    //? if >=1.20.5 {
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}
    //?} else {
    /*@Override
    protected void initDataTracker() {}
    *///?}

    @Override
    public void tick() {
        super.tick();

        //? if >=1.21.2 {
        if (shooter == null || !shooter.isAlive() || !shooter.isGliding()) {
        //?} else {
        /*if (shooter == null || !shooter.isAlive() || !shooter.isFallFlying()) {
        *///?}
            this.discard();
            return;
        }

        life++;
        if (life > lifeTime) {
            this.discard();
            return;
        }

        Vec3d vel = shooter.getVelocity();
        Vec3d look = shooter.getRotationVector();
        double newVelX = vel.x * DAMPING + look.x * FORCE;
        double newVelY = vel.y * DAMPING + look.y * FORCE;
        double newVelZ = vel.z * DAMPING + look.z * FORCE;
        shooter.setVelocity(newVelX, newVelY, newVelZ);
        shooter.velocityModified = true;

        if (getEntityWorld().isClient) {
            Vec3d pos = shooter.getPos();
            for (int i = 0; i < 5; i++) {
                //? if >=1.21.5 {
                getEntityWorld().addParticleClient(ParticleTypes.FLAME,
                        pos.x + (random.nextDouble() - 0.5) * 1.0,
                        pos.y + random.nextDouble() * 1.5,
                        pos.z + (random.nextDouble() - 0.5) * 1.0,
                        0, 0, 0);
                getEntityWorld().addParticleClient(ParticleTypes.LARGE_SMOKE,
                        pos.x + (random.nextDouble() - 0.5) * 1.0,
                        pos.y + random.nextDouble() * 1.5,
                        pos.z + (random.nextDouble() - 0.5) * 1.0,
                        0, 0, 0);
                //?} else {
                /*getEntityWorld().addParticle(ParticleTypes.FLAME,
                        pos.x + (random.nextDouble() - 0.5) * 1.0,
                        pos.y + random.nextDouble() * 1.5,
                        pos.z + (random.nextDouble() - 0.5) * 1.0,
                        0, 0, 0);
                getEntityWorld().addParticle(ParticleTypes.LARGE_SMOKE,
                        pos.x + (random.nextDouble() - 0.5) * 1.0,
                        pos.y + random.nextDouble() * 1.5,
                        pos.z + (random.nextDouble() - 0.5) * 1.0,
                        0, 0, 0);
                *///?}
            }
        }

        Vec3d handOffset = shooter.getHandPosOffset(Items.FIREWORK_ROCKET);
        this.setPosition(shooter.getX() + handOffset.x, shooter.getY() + handOffset.y, shooter.getZ() + handOffset.z);
        this.setVelocity(shooter.getVelocity());
    }

    //? if >=1.21.6 {
    @Override
    protected void readCustomData(ReadView view) {}

    @Override
    protected void writeCustomData(WriteView view) {}
    //?} else {
    /*@Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}
    *///?}

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return false;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}
