package com.sedulous.aslightexpansion.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import com.sedulous.aslightexpansion.FunMod;


public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_ADAMANTIUM_TOOL = createTag("needs_adamantium_tool");
        public static final TagKey<Block> WRONG_FOR_ADAMANTIUM_TOOL = createTag("wrong_for_adamantium_tool");
        public static final TagKey<Block> NEEDS_UNIQUE_TOOL = createTag("needs_unique_tool");
        public static final TagKey<Block> WRONG_FOR_UNIQUE_TOOL = createTag("wrong_for_unique_tool");

        static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(FunMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(FunMod.MOD_ID, name));
        }
    }
}