package com.sedulous.aslightexpansion.event;


import com.sedulous.aslightexpansion.FunMod;
import com.sedulous.aslightexpansion.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.sedulous.aslightexpansion.entity.client.SkeletonKingRenderer;

@Mod.EventBusSubscriber(modid = FunMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SKELETON_KING.get(), SkeletonKingRenderer::new);
    }


    }
