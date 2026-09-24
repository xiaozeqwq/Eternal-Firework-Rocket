package xiaoze_qwq_.eternal_firework_rocket.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

//? if >=1.21.2 {
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
//?}
//? if >=1.21.6 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

public class UltimateFireworkRocketEntity extends Entity {
    private LivingEntity shooter;
    private int life;
    private final int lifeTime = 100;

    private static final double DAMPING = 0.5;
    private static final double FORCE = 12.75;

    public UltimateFireworkRocketEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.setInvisible(true);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public UltimateFireworkRocketEntity(EntityType<?> type, Level world, LivingEntity shooter) {
        this(type, world);
        this.shooter = shooter;
        this.setPos(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    //? if >=1.20.5 {
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}
    //?} else {
    /*@Override
    protected void defineSynchedData() {}
    *///?}

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

        Vec3 vel = shooter.getDeltaMovement();
        Vec3 look = shooter.getLookAngle();
        double newVelX = vel.x * DAMPING + look.x * FORCE;
        double newVelY = vel.y * DAMPING + look.y * FORCE;
        double newVelZ = vel.z * DAMPING + look.z * FORCE;
        shooter.setDeltaMovement(newVelX, newVelY, newVelZ);
        shooter.hurtMarked = true;

        Level entityWorld = level();
        Vec3 pos = shooter.position();
        if (entityWorld.isClientSide()) {
            for (int i = 0; i < 5; i++) {
                entityWorld.addParticle(ParticleTypes.FLAME,
                        pos.x + (random.nextDouble() - 0.5) * 1.0,
                        pos.y + random.nextDouble() * 1.5,
                        pos.z + (random.nextDouble() - 0.5) * 1.0,
                        0, 0, 0);
                entityWorld.addParticle(ParticleTypes.LARGE_SMOKE,
                        pos.x + (random.nextDouble() - 0.5) * 1.0,
                        pos.y + random.nextDouble() * 1.5,
                        pos.z + (random.nextDouble() - 0.5) * 1.0,
                        0, 0, 0);
            }
        }

        Vec3 handOffset = shooter.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);
        this.setPos(shooter.getX() + handOffset.x, shooter.getY() + handOffset.y, shooter.getZ() + handOffset.z);
        this.setDeltaMovement(shooter.getDeltaMovement());
    }

    //? if >=1.21.6 {
    @Override
    protected void readAdditionalSaveData(ValueInput view) {}

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {}
    //?} else {
    /*@Override
    protected void readAdditionalSaveData(CompoundTag nbt) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {}
    *///?}

    //? if >=1.21.2 {
    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return false;
    }
    //?}

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return false;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}
