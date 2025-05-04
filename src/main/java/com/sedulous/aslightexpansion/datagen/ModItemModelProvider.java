package com.sedulous.aslightexpansion.datagen;

import com.sedulous.aslightexpansion.ASlightExpansion;
import com.sedulous.aslightexpansion.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ASlightExpansion.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.ADAMANTIUM.get());
        basicItem(ModItems.ADAMANTIUM_AXE.get());
        basicItem(ModItems.ADAMANTIUM_SWORD.get());
        basicItem(ModItems.ADAMANTIUM_SHOVEL.get());
        basicItem(ModItems.ADAMANTIUM_PICKAXE.get());
    }
}
