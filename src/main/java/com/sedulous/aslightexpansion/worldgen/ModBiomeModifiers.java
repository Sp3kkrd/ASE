package com.sedulous.aslightexpansion.worldgen;

import com.sedulous.aslightexpansion.ASlightExpansion;
import com.sedulous.aslightexpansion.entity.ModEntities;
import io.netty.bootstrap.Bootstrap;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> SPAWN_SAND_SHAMAN = registerKey("spawn_sand_shaman");

    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        var biomes = context.lookup(Registries.BIOME);

    }
    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(ASlightExpansion.MOD_ID, name));
    }
}
