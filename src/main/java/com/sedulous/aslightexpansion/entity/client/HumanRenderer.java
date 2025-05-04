package com.sedulous.aslightexpansion.entity.client;

import com.sedulous.aslightexpansion.ASlightExpansion;
import com.sedulous.aslightexpansion.entity.custom.Human;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HumanRenderer extends MobRenderer<Human, HumanModel<Human>> {
    public HumanRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HumanModel<>(pContext.bakeLayer(HumanModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Human entity) {
        return ResourceLocation.fromNamespaceAndPath(ASlightExpansion.MOD_ID, "textures/entity/human.png");
    }

}
