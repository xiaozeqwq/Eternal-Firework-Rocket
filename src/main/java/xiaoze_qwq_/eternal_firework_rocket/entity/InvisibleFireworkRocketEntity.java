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

public class InvisibleFireworkRocketEntity extends Entity {
    private LivingEntity shooter;
    private int life;
    private int lifeTime;

    public InvisibleFireworkRocketEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.setInvisible(true);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public InvisibleFireworkRocketEntity(EntityType<?> type, Level world, LivingEntity shooter, int lifetime) {
        this(type, world);
        this.shooter = shooter;
        this.lifeTime = lifetime;
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

        if (shooter == null || !shooter.isAlive()) {
            this.discard();
            return;
        }

        if (!shooter.isFallFlying()) {
            this.discard();
            return;
        }

        life++;

        Vec3 rotation = shooter.getLookAngle();
        Vec3 velocity = shooter.getDeltaMovement();
        shooter.setDeltaMovement(
            velocity.add(
                rotation.x * 0.1 + (rotation.x * 1.5 - velocity.x) * 0.5,
                rotation.y * 0.1 + (rotation.y * 1.5 - velocity.y) * 0.5,
                rotation.z * 0.1 + (rotation.z * 1.5 - velocity.z) * 0.5
            )
        );
        shooter.hurtMarked = true;

        Vec3 handOffset = shooter.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);
        this.setPos(shooter.getX() + handOffset.x, shooter.getY() + handOffset.y, shooter.getZ() + handOffset.z);
        this.setDeltaMovement(shooter.getDeltaMovement());

        Level entityWorld = level();
        Vec3 pos = shooter.position();
        if (entityWorld.isClientSide()) {
            entityWorld.addParticle(ParticleTypes.FIREWORK,
                    pos.x + (random.nextDouble() - 0.5) * 0.6,
                    pos.y + random.nextDouble() * 1.2,
                    pos.z + (random.nextDouble() - 0.5) * 0.6,
                    0, 0, 0);
        }

        if (life > lifeTime) {
            this.discard();
        }
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
