package com.sedulous.aslightexpansion.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.ForgeTier;
import com.sedulous.aslightexpansion.util.ModTags;

public class ModToolTiers {
    public static final Tier ADAMANTIUM = new ForgeTier(2500, 4,5,20,
            ModTags.Blocks.NEEDS_ADAMANTIUM_TOOL, () -> Ingredient.of(ModItems.ADAMANTIUM.get()),
            ModTags.Blocks.WRONG_FOR_ADAMANTIUM_TOOL);

    public static final Tier UNIQUE = new ForgeTier(
            Integer.MAX_VALUE,
            4.0f,
            5.0f,
            30,
            ModTags.Blocks.NEEDS_UNIQUE_TOOL,
            () -> Ingredient.EMPTY,
            ModTags.Blocks.WRONG_FOR_UNIQUE_TOOL
    );


}

