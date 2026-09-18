package xiaoze_qwq_.eternal_firework_rocket.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

//? if >=1.21.2 {
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
//?}
//? if >=1.21.6 {
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
//?} else {
/*import net.minecraft.nbt.NbtCompound;
*///?}

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

        if (shooter == null || !shooter.isAlive()) {
            this.discard();
            return;
        }

        //? if >=1.21.2 {
        if (!shooter.isGliding()) {
        //?} else {
        /*if (!shooter.isFallFlying()) {
        *///?}
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
        //? if >=1.21.11 {
        shooter.knockedBack = true;
        //?} else {
        /*shooter.velocityModified = true;
        *///?}

        Vec3d handOffset = shooter.getHandPosOffset(Items.FIREWORK_ROCKET);
        this.setPosition(shooter.getX() + handOffset.x, shooter.getY() + handOffset.y, shooter.getZ() + handOffset.z);
        this.setVelocity(shooter.getVelocity());

        //? if >=1.21.9 {
        World entityWorld = getEntityWorld();
        Vec3d pos = shooter.getEntityPos();
        //?} else {
        /*World entityWorld = getWorld();
        Vec3d pos = shooter.getPos();
        *///?}
        if (entityWorld.isClient()) {
            //? if >=1.21.5 {
            entityWorld.addParticleClient(ParticleTypes.FIREWORK,
                    pos.x + (random.nextDouble() - 0.5) * 0.6,
                    pos.y + random.nextDouble() * 1.2,
                    pos.z + (random.nextDouble() - 0.5) * 0.6,
                    0, 0, 0);
            //?} else {
            /*entityWorld.addParticle(ParticleTypes.FIREWORK,
                    pos.x + (random.nextDouble() - 0.5) * 0.6,
                    pos.y + random.nextDouble() * 1.2,
                    pos.z + (random.nextDouble() - 0.5) * 0.6,
                    0, 0, 0);
            *///?}
        }

        if (life > lifeTime) {
            this.discard();
        }
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

    //? if >=1.21.2 {
    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }
    //?}

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return false;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}
