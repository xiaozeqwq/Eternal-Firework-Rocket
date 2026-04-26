package xiaoze_qwq_.eternal_firework_rocket.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import xiaoze_qwq_.eternal_firework_rocket.EternalFireworkRocket;
import xiaoze_qwq_.eternal_firework_rocket.client.render.InvisibleFireworkRocketEntityRenderer;
import xiaoze_qwq_.eternal_firework_rocket.client.render.UltimateFireworkRocketEntityRenderer;

public class EternalFireworkRocketClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 注册实体渲染器（不可见）
        EntityRendererRegistry.register(EternalFireworkRocket.INVISIBLE_FIREWORK_ROCKET,
                InvisibleFireworkRocketEntityRenderer::new);
        EntityRendererRegistry.register(EternalFireworkRocket.ULTIMATE_FIREWORK_ROCKET,
                UltimateFireworkRocketEntityRenderer::new);
    }
}