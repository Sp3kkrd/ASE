package com.sedulous.aslightexpansion.entity.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class SnowballGunProjectile extends Snowball {
    public SnowballGunProjectile(EntityType<? extends Snowball> type, Level level) {
        super(type, level);
    }

    public SnowballGunProjectile(Level level, LivingEntity shooter) {
        super(level, shooter);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (!this.level().isClientSide) {
            if (result.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.hurt(this.damageSources().thrown(this, this.getOwner()), 3.0F);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0)); // Slowness II for 3 seconds
            }
        }
    }
}

