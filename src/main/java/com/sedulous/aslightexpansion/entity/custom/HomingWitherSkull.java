package com.sedulous.aslightexpansion.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;

public class HomingWitherSkull extends WitherSkull {
    private LivingEntity target;

    public HomingWitherSkull(Level level, LivingEntity shooter) {
        super(EntityType.WITHER_SKULL, level);
        this.setOwner(shooter);
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public void tick() {
        super.tick();

        if (target != null && !target.isDeadOrDying()) {
            double dx = target.getX() - this.getX();
            double dy = target.getY() + target.getBbHeight() / 2 - this.getY();
            double dz = target.getZ() - this.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (distance > 0) {
                this.setDeltaMovement(dx / distance * 0.5, dy / distance * 0.5, dz / distance * 0.5);
            }

            if (this.distanceToSqr(target) < 1.0) {
                target.hurt(damageSources().magic(),10.0F);
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1)); // 5 seconds
                this.discard();
            }
        } else {
            this.discard();
        }
    }
}
