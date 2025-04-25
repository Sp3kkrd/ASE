package com.sedulous.aslightexpansion.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.sedulous.aslightexpansion.FunMod;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FunMod.MOD_ID);

    public static final RegistryObject<Item> ADAMANTIUM = ITEMS.register("adamantium",
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ADAMANTIUM_SWORD = ITEMS.register("adamantium_sword",
    () -> new SwordItem(ModToolTiers.ADAMANTIUM, new Item.Properties()
            .attributes(SwordItem.createAttributes(ModToolTiers.ADAMANTIUM, 3, -2.4f))));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
