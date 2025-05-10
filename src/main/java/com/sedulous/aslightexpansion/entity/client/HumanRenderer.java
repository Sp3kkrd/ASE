package com.sedulous.aslightexpansion.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.sedulous.aslightexpansion.ASlightExpansion;
import com.sedulous.aslightexpansion.entity.custom.Human;
import com.sedulous.aslightexpansion.entity.custom.SandShaman;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HumanRenderer extends MobRenderer<Human, HumanModel<Human>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("aslightexpansion", "textures/entity/human.png");

    public HumanRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanModel<>(context.bakeLayer(HumanModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new HumanHeldItemLayer(this));

    }


    @Override
    public ResourceLocation getTextureLocation(Human entity) {
        if (entity instanceof SandShaman) {
            return ResourceLocation.fromNamespaceAndPath(ASlightExpansion.MOD_ID,"textures/entity/sand_shaman.png");
        }
        return ResourceLocation.fromNamespaceAndPath(ASlightExpansion.MOD_ID,"textures/entity/human.png");
    }

    public class HumanHeldItemLayer extends RenderLayer<Human, HumanModel<Human>> {
        public HumanHeldItemLayer(RenderLayerParent<Human, HumanModel<Human>> parent) {
            super(parent);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                           Human entity, float limbSwing, float limbSwingAmount, float partialTicks,
                           float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack stack = entity.getMainHandItem();
            if (stack.isEmpty()) return;

            poseStack.pushPose();

            // Move to hand
            HumanModel<Human> model = this.getParentModel();
            model.right_arm.translateAndRotate(poseStack); // Adjust to match your arm part

            // Offset & rotate if needed
            poseStack.translate(-0.05F, 0.6F, -0.1F); // Tweak these values for better alignment
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    entity.level(),
                    entity.getId()
            );

            poseStack.popPose();
        }
    }

}
