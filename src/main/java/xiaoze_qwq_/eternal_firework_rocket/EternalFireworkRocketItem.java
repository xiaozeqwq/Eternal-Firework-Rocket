package xiaoze_qwq_.eternal_firework_rocket;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;

import java.util.List;

public class EternalFireworkRocketItem extends FireworkRocketItem {
    
    public EternalFireworkRocketItem(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        
        // 管理员检查
        if (ModConfig.CONFIG.adminOnly && user instanceof ServerPlayerEntity serverPlayer) {
            if (!serverPlayer.hasPermissionLevel(2)) {
                return TypedActionResult.fail(itemStack);
            }
        }
        
        if (!world.isClient) {
            // 确定飞行时间（1、2或3）
            int flightDuration = itemStack.isOf(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_3) ? 3 : 
                                itemStack.isOf(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_2) ? 2 : 1;
            
            // 计算冷却时间
            float cooldownTime = flightDuration - ModConfig.CONFIG.cooldownOffset;
            if (cooldownTime < 0.1f) cooldownTime = 0.1f;
            
            // 设置冷却
            user.getItemCooldownManager().set(this, (int)(cooldownTime * 20));
            
            // 创建烟花火箭的物品堆栈 - 1.20.6 使用组件系统
            ItemStack fireworkStack = new ItemStack(this);
            
            // 设置飞行时间组件，爆炸列表为空
            FireworksComponent fireworksComponent = new FireworksComponent(flightDuration, List.of());
            fireworkStack.set(DataComponentTypes.FIREWORKS, fireworksComponent);
            
            // 创建并发射烟花
            net.minecraft.entity.projectile.FireworkRocketEntity fireworkRocketEntity = 
                new net.minecraft.entity.projectile.FireworkRocketEntity(world, fireworkStack, user);
            world.spawnEntity(fireworkRocketEntity);
            
            // 播放声音
            world.playSound(null, user.getX(), user.getY(), user.getZ(), 
                SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 3.0f, 1.0f);
            
            // 统计但不消耗
            user.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        
        return TypedActionResult.success(itemStack, world.isClient());
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}