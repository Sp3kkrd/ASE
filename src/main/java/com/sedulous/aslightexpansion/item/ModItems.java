package com.sedulous.aslightexpansion.item;

import com.sedulous.aslightexpansion.item.custom.*;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.sedulous.aslightexpansion.FunMod;

import static com.sedulous.aslightexpansion.item.ModToolTiers.UNIQUE;
import static net.minecraft.world.item.Tiers.DIAMOND;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FunMod.MOD_ID);
    //Minerals
    public static final RegistryObject<Item> ADAMANTIUM = ITEMS.register("adamantium",
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ADAMANTIUM_NUGGET = ITEMS.register("adamantium_nugget",
            () -> new Item(new Item.Properties()));

    //Mineral Weapons
    public static final RegistryObject<Item> ADAMANTIUM_SWORD = ITEMS.register("adamantium_sword",
    () -> new SwordItem(ModToolTiers.ADAMANTIUM, new Item.Properties()
            .attributes(SwordItem.createAttributes(ModToolTiers.ADAMANTIUM, 3, -2.4f))));
    public static final RegistryObject<Item> ADAMANTIUM_AXE = ITEMS.register("adamantium_axe",
            () -> new AxeItem(ModToolTiers.ADAMANTIUM, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.ADAMANTIUM, 6, -3.2f))));
    public static final RegistryObject<Item> ADAMANTIUM_PICKAXE = ITEMS.register("adamantium_pickaxe",
            () -> new PickaxeItem(ModToolTiers.ADAMANTIUM, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.ADAMANTIUM, 1, -2.8f))));
    public static final RegistryObject<Item> ADAMANTIUM_SHOVEL = ITEMS.register("adamantium_shovel",
            () -> new ShovelItem(ModToolTiers.ADAMANTIUM, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.ADAMANTIUM, 1.5f, -3.0f))));

    //Unique Weapons
    public static final RegistryObject<Item> KINGS_CLAYMORE = ITEMS.register("kings_claymore",
            () -> new SwordItem(DIAMOND,new Item.Properties()
                    .attributes(SwordItem.createAttributes(DIAMOND,13, -2.6f))));

    public static final RegistryObject<Item> MINDS_EYE = ITEMS.register("minds_eye",
                () -> new MindsEye(UNIQUE,new Item.Properties()
                        .attributes(SwordItem.createAttributes(UNIQUE,10, -3f))));

    public static final RegistryObject<Item> THE_CONDUIT = ITEMS.register("the_conduit",
            () -> new TheConduit(UNIQUE,new Item.Properties()
                    .attributes(SwordItem.createAttributes(UNIQUE,10, -3f))));

        //Unique Offhand
        public static final RegistryObject<Item> MAGMA_RING = ITEMS.register("magma_ring",
                () -> new MagmaRing(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

        public static final RegistryObject<Item> PRIM_SNOW = ITEMS.register("primordial_snowglobe",
                () -> new PrimordialSnowglobe(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

        public static final RegistryObject<Item> TALIS_FER = ITEMS.register("ferocity_talisman",
             () -> new FerocityTalisman(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> KINGS_HELMET = ITEMS.register("kings_helmet",
            () -> new ArmorItem(ModArmorMaterials.KINGS_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(Integer.MAX_VALUE))));
    public static final RegistryObject<Item> KINGS_CHESTPLATE = ITEMS.register("kings_chestplate",
            () -> new ArmorItem(ModArmorMaterials.KINGS_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(Integer.MAX_VALUE))));
    public static final RegistryObject<Item> KINGS_LEGGINGS = ITEMS.register("kings_leggings",
            () -> new ArmorItem(ModArmorMaterials.KINGS_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(Integer.MAX_VALUE))));
    public static final RegistryObject<Item> KINGS_BOOTS = ITEMS.register("kings_boots",
            () -> new ArmorItem(ModArmorMaterials.KINGS_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(Integer.MAX_VALUE))));
    @SubscribeEvent
        public static void commonSetup(final FMLCommonSetupEvent event) {
            // Any common setup logic can go here

    }


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
