package xiaoze_qwq_.eternal_firework_rocket.client.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import xiaoze_qwq_.eternal_firework_rocket.entity.UltimateFireworkRocketEntity;

//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.state.EntityRenderState;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
*///?}

//? if >=1.21.2 {
public class UltimateFireworkRocketEntityRenderer extends EntityRenderer<UltimateFireworkRocketEntity, EntityRenderState> {
    public UltimateFireworkRocketEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
//?} else {
/*public class UltimateFireworkRocketEntityRenderer extends EntityRenderer<UltimateFireworkRocketEntity> {
    public UltimateFireworkRocketEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(UltimateFireworkRocketEntity entity) {
        return null;
    }

    @Override
    public void render(UltimateFireworkRocketEntity entity, float yaw, float tickDelta,
                       PoseStack matrices,
                       MultiBufferSource vertexConsumers, int light) {
    }
}
*///?}
