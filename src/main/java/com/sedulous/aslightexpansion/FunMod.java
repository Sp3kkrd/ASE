package com.sedulous.aslightexpansion;

import com.mojang.logging.LogUtils;
import com.sedulous.aslightexpansion.block.ModBlocks;


import com.sedulous.aslightexpansion.entity.ModEntities;
import com.sedulous.aslightexpansion.entity.custom.SkeletonKing;
import com.sedulous.aslightexpansion.item.ModItems;
import com.sedulous.aslightexpansion.sound.ModSounds;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FunMod.MOD_ID)
public class FunMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "funmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();



    public FunMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(this::registerAttributes);
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.ADAMANTIUM);
            event.accept(ModItems.ADAMANTIUM_NUGGET);
        }

        if(event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.ADAMANTIUM_SWORD);
            event.accept(ModItems.KINGS_CLAYMORE);
            event.accept(ModItems.MINDS_EYE);
            event.accept(ModItems.THE_CONDUIT);
            event.accept(ModItems.MAGMA_RING);
            event.accept(ModItems.PRIM_SNOW);
            event.accept(ModItems.TALIS_FER);

            event.accept(ModItems.SNOWBALL_GUN);
            event.accept(ModItems.AUTO_SNOWBALL_CANNON);
            event.accept(ModItems.MAKESHIFT_GUN);
            event.accept(ModItems.MAKESHIFT_REPEATER);
            event.accept(ModItems.ABADDON);

            event.accept(ModItems.KINGS_HELMET);
            event.accept(ModItems.KINGS_CHESTPLATE);
            event.accept(ModItems.KINGS_LEGGINGS);
            event.accept(ModItems.KINGS_BOOTS);
        }

        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.ADAMANTIUM_AXE);
            event.accept(ModItems.ADAMANTIUM_SHOVEL);
            event.accept(ModItems.ADAMANTIUM_PICKAXE);
        }

        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.ADAMANTIUM_BLOCK);
        }
    }
    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SKELETON_KING.get(), SkeletonKing.createAttributes().build());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
