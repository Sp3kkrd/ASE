package com.sedulous.aslightexpansion.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import com.sedulous.aslightexpansion.util.ModTags;

public class ModToolTiers {
    public static final Tier ADAMANTIUM = new ForgeTier(2500, 4,7,20,
            ModTags.Blocks.NEEDS_ADAMANTIUM_TOOL, () -> Ingredient.of(ModItems.ADAMANTIUM.get()),
            ModTags.Blocks.WRONG_FOR_ADAMANTIUM_TOOL);
}
