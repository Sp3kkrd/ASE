package com.sedulous.aslightexpansion.entity;

import com.sedulous.aslightexpansion.FunMod;
import com.sedulous.aslightexpansion.entity.custom.SkeletonKing;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = FunMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FunMod.MOD_ID);

    public static final RegistryObject<EntityType<SkeletonKing>> SKELETON_KING = ENTITY_TYPES.register("skeleton_king",
            () -> EntityType.Builder.of(SkeletonKing::new, MobCategory.MONSTER)
                    .sized(0.9F, 2.9F)
                    .build("skeleton_king"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
    }