package xiaoze_qwq_.eternal_firework_rocket.client.render;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import xiaoze_qwq_.eternal_firework_rocket.entity.UltimateFireworkRocketEntity;

//? if >=1.21.2 {
import net.minecraft.client.render.entity.state.EntityRenderState;
//?} else {
/*import net.minecraft.util.Identifier;
*///?}

//? if >=1.21.2 {
public class UltimateFireworkRocketEntityRenderer extends EntityRenderer<UltimateFireworkRocketEntity, EntityRenderState> {
    public UltimateFireworkRocketEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
//?} else {
/*public class UltimateFireworkRocketEntityRenderer extends EntityRenderer<UltimateFireworkRocketEntity> {
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
    }
}
*///?}
