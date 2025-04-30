package com.sedulous.aslightexpansion.entity.custom;


import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class LargeSnowballGunProjectile extends Snowball {
    public LargeSnowballGunProjectile(EntityType<? extends Snowball> type, Level level) {
        super(type, level);
    }

    public LargeSnowballGunProjectile(Level level, LivingEntity shooter) {
        super(level, shooter);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (!this.level().isClientSide) {
            if (result.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.hurt(this.damageSources().thrown(this, this.getOwner()), 6.0F);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1)); // Slowness II for 3 seconds
            }
        }
    }
    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            // Spawn snowflake or cloud particles around the projectile
            this.level().addParticle(
                    net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL, // Looks like snowball bits
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    (this.random.nextFloat() - 0.5) * 0.1,
                    (this.random.nextFloat() - 0.5) * 0.1,
                    (this.random.nextFloat() - 0.5) * 0.1
            );
        }
    }

}


