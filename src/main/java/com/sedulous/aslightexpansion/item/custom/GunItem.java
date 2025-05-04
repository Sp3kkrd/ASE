package com.sedulous.aslightexpansion.item.custom;

import com.sedulous.aslightexpansion.item.ModItems;
import com.sedulous.aslightexpansion.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GunItem extends Item {
    protected final float damage;
    protected final float range;
    protected final int cooldownTicks;

    public GunItem(float damage, float range, int fireRateTicks, Properties properties) {
        super(properties);
        this.damage = damage;
        this.range = range;
        this.cooldownTicks = fireRateTicks;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Check for bullet in inventory
            boolean hasBullet = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack invStack = player.getInventory().getItem(i);
                if (invStack.is(ModItems.BULLET.get())) {
                    invStack.shrink(1); // Consume bullet
                    hasBullet = true;
                    break;
                }
            }

            // If no bullet, do nothing
            if (!hasBullet) {
                return InteractionResultHolder.fail(stack);
            }

            // Proceed with firing
            if (level instanceof ServerLevel serverLevel) {
                EntityHitResult hitResult = raycastEntities(serverLevel, player, range);
                if (hitResult != null && hitResult.getEntity() instanceof LivingEntity target) {
                    DamageSources sources = serverLevel.damageSources();
                    DamageSource source = sources.playerAttack(player);
                    applyGunDamage(target, source, damage);
                }

                spawnParticles(serverLevel, player, range);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.GUNSHOT.get(), SoundSource.PLAYERS, 0.5f, 1.0f);
            player.getCooldowns().addCooldown(this, cooldownTicks);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    protected EntityHitResult raycastEntities(ServerLevel level, Player player, double range) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle();
        Vec3 reachVec = eyePos.add(look.scale(range));

        AABB box = player.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0D);
        return net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult(
                level, player, eyePos, reachVec, box, e -> e instanceof LivingEntity && e != player
        );
    }

    protected void spawnParticles(ServerLevel level, Player player, double range) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle();

        for (int i = 0; i <= 10; i++) {
            double progress = i / 10.0;
            Vec3 point = eyePos.add(look.scale(range * progress));
            level.sendParticles(ParticleTypes.CRIT, point.x, point.y, point.z, 1, 0, 0, 0, 0);
        }
    }

    protected void applyGunDamage(LivingEntity target, DamageSource source, float amount) {
        if (target.hurt(source, amount)) {
            // Cancel knockback-like movement from being hurt
            Vec3 motion = target.getDeltaMovement();
            target.setDeltaMovement(0, motion.y, 0);  // Preserve Y axis to avoid interfering with jumping/falling
            target.hurtMarked = true; // Ensures client sync

            // Reduce i-frames to allow faster follow-up shots
            target.invulnerableTime = 2; // Default is 10; tweak as needed
        }
    }


    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        if (shouldShowBaseTooltip()) {
            pTooltipComponents.add(Component.literal(""));
            pTooltipComponents.add(Component.literal("§7When held:"));
            pTooltipComponents.add(Component.literal("§2 " + damage + "§2 Damage"));
            pTooltipComponents.add(Component.literal("§2 " + range + "§2 Range"));
            pTooltipComponents.add(Component.literal("§2 " + Mth.floor(20f / cooldownTicks) + "§2 Fire Rate"));
        }
    }
    protected boolean shouldShowBaseTooltip() {
        return true;
    }
}
