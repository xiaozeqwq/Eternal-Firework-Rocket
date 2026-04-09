package xiaoze_qwq_.eternal_firework_rocket.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class InvisibleFireworkRocketEntity extends Entity {
    private LivingEntity shooter;
    private int lifetime;
    private int age;

    public InvisibleFireworkRocketEntity(EntityType<?> type, World world) {
        super(type, world);
        this.setInvisible(true);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public InvisibleFireworkRocketEntity(EntityType<?> type, World world, LivingEntity shooter, int lifetime) {
        this(type, world);
        this.shooter = shooter;
        this.lifetime = lifetime;
        this.setPosition(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    public void tick() {
        super.tick();
        age++;

        // 销毁条件：年龄超限、射手无效、射手死亡或不再飞行
        if (age >= lifetime || shooter == null || !shooter.isAlive() || !shooter.isFallFlying()) {
            this.discard();
            return;
        }

        if (!this.getWorld().isClient) {
            double thrust = 0.15; // 每 tick 推力（原版约 0.15～0.25）
            Vec3d direction = shooter.getRotationVector(); // 实时获取当前视线方向

            if (age == 1) {
                // 初始瞬间脉冲（5倍推力，模拟原版起飞加速）
                shooter.addVelocity(
                    direction.x * thrust * 5,
                    direction.y * thrust * 5,
                    direction.z * thrust * 5
                );
            } else {
                shooter.addVelocity(
                    direction.x * thrust,
                    direction.y * thrust,
                    direction.z * thrust
                );
            }

            shooter.velocityModified = true; // 同步速度
        }

        // 火箭位置始终跟随玩家
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