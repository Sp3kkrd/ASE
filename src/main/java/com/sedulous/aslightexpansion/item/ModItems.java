package com.sedulous.aslightexpansion.item;

import com.sedulous.aslightexpansion.item.custom.*;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.sedulous.aslightexpansion.ASlightExpansion;

import static com.sedulous.aslightexpansion.item.ModToolTiers.UNIQUE;
import static net.minecraft.world.item.Tiers.DIAMOND;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ASlightExpansion.MOD_ID);
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
                    .attributes(SwordItem.createAttributes(DIAMOND,6, -2.6f))));

    public static final RegistryObject<Item> MINDS_EYE = ITEMS.register("minds_eye",
                () -> new MindsEye(UNIQUE,new Item.Properties()
                        .attributes(SwordItem.createAttributes(UNIQUE,10, -3f))));

    public static final RegistryObject<Item> THE_CONDUIT = ITEMS.register("the_conduit",
            () -> new TheConduit(UNIQUE,new Item.Properties()
                    .attributes(SwordItem.createAttributes(UNIQUE,10, -3f))));

    public static final RegistryObject<Item> SNOWBALL_GUN = ITEMS.register("snowball_gun",
            () -> new SnowballGun(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> AUTO_SNOWBALL_CANNON = ITEMS.register("auto_snowball_cannon",
            () -> new AutoSnowballCannon(new Item.Properties().stacksTo(1).rarity(Rarity.RARE))); // Fire rate set to 10 ticks

        //Unique Offhand
        public static final RegistryObject<Item> MAGMA_RING = ITEMS.register("magma_ring",
                () -> new MagmaRing(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

        public static final RegistryObject<Item> PRIM_SNOW = ITEMS.register("primordial_snowglobe",
                () -> new PrimordialSnowglobe(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

        public static final RegistryObject<Item> TALIS_FER = ITEMS.register("ferocity_talisman",
             () -> new FerocityTalisman(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

        //Staffs
        public static final RegistryObject<Item> SAND_SCOURGE_STAFF = ITEMS.register("sand_scourge_staff",
                () -> new SandScourgeStaff(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

        //Guns
        public static final RegistryObject<Item> BULLET = ITEMS.register("bullet",
                () -> new Item(new Item.Properties().stacksTo(99)));

    public static final RegistryObject<Item> MAKESHIFT_GUN = ITEMS.register("makeshift_gun",
            () -> new GunItem(2,15, 13, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)));

    public static final RegistryObject<Item> MAKESHIFT_REPEATER = ITEMS.register("makeshift_repeater",
            () -> new GunItem(3,30, 9, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)));

    public static final RegistryObject<Item> MACHINE_GUN = ITEMS.register("machine_gun",
            () -> new GunItem(3,20, 4, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)));

        //Unique Guns
    public static final RegistryObject<Item> ABADDON = ITEMS.register("abaddon",
            () -> new Abaddon(11,20, 6, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> NECROMANCER = ITEMS.register("necromancer",
            () -> new Necromancer(8,15, 4, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> BONECHILLER = ITEMS.register("bonechiller",
            () -> new Bonechiller(15,30, 20, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    //Unique Armor
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

    public static final RegistryObject<Item> NATURE_HELMET = ITEMS.register("nature_helmet",
            () -> new NatureArmorItem(ModArmorMaterials.NATURE_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(Integer.MAX_VALUE))));
    public static final RegistryObject<Item> NATURE_CHESTPLATE = ITEMS.register("nature_chestplate",
            () -> new NatureArmorItem(ModArmorMaterials.NATURE_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(Integer.MAX_VALUE))));
    public static final RegistryObject<Item> NATURE_LEGGINGS = ITEMS.register("nature_leggings",
            () -> new NatureArmorItem(ModArmorMaterials.NATURE_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(Integer.MAX_VALUE))));
    public static final RegistryObject<Item> NATURE_BOOTS = ITEMS.register("nature_boots",
            () -> new NatureArmorItem(ModArmorMaterials.NATURE_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(Integer.MAX_VALUE))));

    public static final RegistryObject<Item> FREELANCER_HELMET = ITEMS.register("freelancer_helmet",
            () -> new ArmorItem(ModArmorMaterials.FREELANCER_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(Integer.MAX_VALUE))));
    public static final RegistryObject<Item> FREELANCER_CHESTPLATE = ITEMS.register("freelancer_chestplate",
            () -> new ArmorItem(ModArmorMaterials.FREELANCER_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(Integer.MAX_VALUE))));
    public static final RegistryObject<Item> FREELANCER_LEGGINGS = ITEMS.register("freelancer_leggings",
            () -> new ArmorItem(ModArmorMaterials.FREELANCER_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(Integer.MAX_VALUE))));
    public static final RegistryObject<Item> FREELANCER_BOOTS = ITEMS.register("freelancer_boots",
            () -> new ArmorItem(ModArmorMaterials.FREELANCER_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(Integer.MAX_VALUE))));

    @SubscribeEvent
        public static void commonSetup(final FMLCommonSetupEvent event) {
            // Any common setup logic can go here

    }


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
