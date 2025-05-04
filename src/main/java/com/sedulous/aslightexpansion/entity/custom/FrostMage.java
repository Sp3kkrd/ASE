package com.sedulous.aslightexpansion.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.level.Level;

public abstract class FrostMage extends AbstractSkeleton {
    public FrostMage(EntityType<? extends AbstractSkeleton> type, Level level) {
        super(type, level);
    }

    // You can override AI, sounds, etc.
}
