package xiaoze_qwq_.eternal_firework_rocket.client.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import xiaoze_qwq_.eternal_firework_rocket.entity.InvisibleFireworkRocketEntity;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.EntityRenderState;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
*///?}

//? if >=1.21.2 {
public class InvisibleFireworkRocketEntityRenderer extends EntityRenderer<InvisibleFireworkRocketEntity, EntityRenderState> {
    public InvisibleFireworkRocketEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
//?} else {
/*public class InvisibleFireworkRocketEntityRenderer extends EntityRenderer<InvisibleFireworkRocketEntity> {
    public InvisibleFireworkRocketEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(InvisibleFireworkRocketEntity entity) {
        return null;
    }

    @Override
    public void render(InvisibleFireworkRocketEntity entity, float yaw, float tickDelta,
                       PoseStack matrices,
                       MultiBufferSource vertexConsumers, int light) {
    }
}
*///?}
