package com.sedulous.aslightexpansion.event;


import com.sedulous.aslightexpansion.ASlightExpansion;
import com.sedulous.aslightexpansion.entity.ModEntities;
import com.sedulous.aslightexpansion.entity.client.HumanModel;
import com.sedulous.aslightexpansion.entity.client.HumanRenderer;
import com.sedulous.aslightexpansion.entity.custom.Human;
import com.sedulous.aslightexpansion.entity.custom.SandShaman;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.sedulous.aslightexpansion.entity.client.SkeletonKingRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

@Mod.EventBusSubscriber(modid = ASlightExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SKELETON_KING.get(), SkeletonKingRenderer::new);

        event.registerEntityRenderer(ModEntities.SNOWBALL_GUN_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.LARGE_SNOWBALL_GUN_PROJECTILE.get(), ThrownItemRenderer::new);

        event.registerEntityRenderer(ModEntities.HUMAN.get(), HumanRenderer::new);

        event.registerEntityRenderer(ModEntities.SAND_SHAMAN.get(), HumanRenderer::new);




    }
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HumanModel.LAYER_LOCATION, HumanModel::createBodyLayer);

    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HUMAN.get(), Human.createAttributes().build());
        event.put(ModEntities.SAND_SHAMAN.get(), SandShaman.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.SAND_SHAMAN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

    }



}



