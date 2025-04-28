package com.sedulous.aslightexpansion.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sedulous.aslightexpansion.FunMod;
import com.sedulous.aslightexpansion.entity.custom.SkeletonKing;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SkeletonKingRenderer extends MobRenderer<SkeletonKing, SkeletonModel<SkeletonKing>> {
    public SkeletonKingRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON)), 0.5f);

        // Add armor layer
        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)),
                new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR)),
                context.getModelManager()
        ));

        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(SkeletonKing skeletonKing) {
        return ResourceLocation.fromNamespaceAndPath(FunMod.MOD_ID, "textures/entity/skeleton_king.png");
    }

    @Override
    protected void scale(SkeletonKing skeletonKing, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.5f, 1.5f, 1.5f); // Make him 1.5x bigger!
        super.scale(skeletonKing, poseStack, partialTickTime);
    }
}