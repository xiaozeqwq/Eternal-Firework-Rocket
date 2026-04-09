package xiaoze_qwq_.eternal_firework_rocket.client.render;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import xiaoze_qwq_.eternal_firework_rocket.entity.InvisibleFireworkRocketEntity;

public class InvisibleFireworkRocketEntityRenderer extends EntityRenderer<InvisibleFireworkRocketEntity> {
    public InvisibleFireworkRocketEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(InvisibleFireworkRocketEntity entity) {
        return null; // 不会被调用
    }

    @Override
    public void render(InvisibleFireworkRocketEntity entity, float yaw, float tickDelta,
                       net.minecraft.client.util.math.MatrixStack matrices,
                       net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light) {
        // 完全不渲染
    }
}