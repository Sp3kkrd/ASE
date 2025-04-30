package com.sedulous.aslightexpansion.event;


import com.sedulous.aslightexpansion.FunMod;
import com.sedulous.aslightexpansion.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.sedulous.aslightexpansion.entity.client.SkeletonKingRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

@Mod.EventBusSubscriber(modid = FunMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SKELETON_KING.get(), SkeletonKingRenderer::new);

        event.registerEntityRenderer(ModEntities.SNOWBALL_GUN_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.LARGE_SNOWBALL_GUN_PROJECTILE.get(), ThrownItemRenderer::new);


    }


    }
