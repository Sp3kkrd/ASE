package com.sedulous.aslightexpansion.entity;

import com.sedulous.aslightexpansion.ASlightExpansion;
import com.sedulous.aslightexpansion.entity.custom.Human;
import com.sedulous.aslightexpansion.entity.custom.SkeletonKing;
import com.sedulous.aslightexpansion.entity.custom.LargeSnowballGunProjectile;
import com.sedulous.aslightexpansion.entity.custom.SnowballGunProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ASlightExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ASlightExpansion.MOD_ID);

    public static final RegistryObject<EntityType<SkeletonKing>> SKELETON_KING = ENTITY_TYPES.register("skeleton_king",
            () -> EntityType.Builder.of(SkeletonKing::new, MobCategory.MONSTER)
                    .sized(0.9F, 2.9F)
                    .build("skeleton_king"));

    public static final RegistryObject<EntityType<SnowballGunProjectile>> SNOWBALL_GUN_PROJECTILE =
            ENTITY_TYPES.register("snowball_gun_projectile",
                    () -> EntityType.Builder.<SnowballGunProjectile>of(SnowballGunProjectile::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(ResourceLocation.fromNamespaceAndPath(ASlightExpansion.MOD_ID, "snowball_gun_projectile").toString()));

    public static final RegistryObject<EntityType<LargeSnowballGunProjectile>> LARGE_SNOWBALL_GUN_PROJECTILE =
            ENTITY_TYPES.register("large_snowball_gun_projectile",
                    () -> EntityType.Builder.<LargeSnowballGunProjectile>of(LargeSnowballGunProjectile::new, MobCategory.MISC)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(ResourceLocation.fromNamespaceAndPath(ASlightExpansion.MOD_ID, "large_snowball_gun_projectile").toString()));

    public static final RegistryObject<EntityType<Human>> HUMAN =
            ENTITY_TYPES.register("human", 
                    () -> EntityType.Builder.of(Human::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.96f)
                            .build(ResourceLocation.fromNamespaceAndPath(ASlightExpansion.MOD_ID, "human").toString()));


    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
    }