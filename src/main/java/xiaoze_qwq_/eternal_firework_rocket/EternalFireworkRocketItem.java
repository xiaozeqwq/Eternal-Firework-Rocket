package xiaoze_qwq_.eternal_firework_rocket;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import xiaoze_qwq_.eternal_firework_rocket.config.ModConfig;

public class EternalFireworkRocketItem extends FireworkRocketItem {
    
    public EternalFireworkRocketItem(Item.Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        
        // 管理员检查
        if (ModConfig.CONFIG.adminOnly && user instanceof ServerPlayerEntity serverPlayer) {
            if (!serverPlayer.hasPermissionLevel(2)) {
                return ActionResult.FAIL;
            }
        }
        
        if (!world.isClient()) {
            // 确定飞行时间（1、2或3）
            int flightDuration = itemStack.isOf(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_3) ? 3 : 
                                itemStack.isOf(EternalFireworkRocket.ETERNAL_FIREWORK_ROCKET_2) ? 2 : 1;
            
            // 计算冷却时间
            float cooldownTime = flightDuration - ModConfig.CONFIG.cooldownOffset;
            if (cooldownTime < 0.1f) cooldownTime = 0.1f;
            
            // 设置冷却
            user.getItemCooldownManager().set(itemStack, (int)(cooldownTime * 20));
            
            // 创建并发射烟花
            FireworksComponent fireworksComponent = new FireworksComponent(flightDuration, java.util.Collections.emptyList());
            ItemStack fireworkStack = new ItemStack(this);
            fireworkStack.set(DataComponentTypes.FIREWORKS, fireworksComponent);
            
            net.minecraft.entity.projectile.FireworkRocketEntity fireworkRocketEntity = 
                new net.minecraft.entity.projectile.FireworkRocketEntity(world, fireworkStack, user);
            world.spawnEntity(fireworkRocketEntity);
            
            // 播放声音
            world.playSound(null, user.getX(), user.getY(), user.getZ(), 
                SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 3.0f, 1.0f);
            
            // 统计但不消耗
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            
            // 返回服务器端的成功结果
            return ActionResult.CONSUME;
        }
        
        // 返回客户端的成功结果
        return ActionResult.SUCCESS;
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}