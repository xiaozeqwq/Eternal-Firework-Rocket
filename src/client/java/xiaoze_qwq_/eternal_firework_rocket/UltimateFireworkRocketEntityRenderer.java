package xiaoze_qwq_.eternal_firework_rocket.client.render;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import xiaoze_qwq_.eternal_firework_rocket.entity.UltimateFireworkRocketEntity;

public class UltimateFireworkRocketEntityRenderer extends EntityRenderer<UltimateFireworkRocketEntity> {
    public UltimateFireworkRocketEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(UltimateFireworkRocketEntity entity) {
        return null;
    }

    @Override
    public void render(UltimateFireworkRocketEntity entity, float yaw, float tickDelta,
                       net.minecraft.client.util.math.MatrixStack matrices,
                       net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light) {
        // 完全不渲染
    }
}