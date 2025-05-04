package com.sedulous.aslightexpansion.item.custom;

import com.sedulous.aslightexpansion.item.ModItems;
import com.sedulous.aslightexpansion.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Bonechiller extends GunItem {

    public Bonechiller(float damage, float range, int cooldownTicks, Properties properties) {
        super(damage, range, cooldownTicks, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
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

            if (!hasBullet) {
                return InteractionResultHolder.fail(stack);
            }

            EntityHitResult hitResult = raycastEntities(serverLevel, player, range);

            if (hitResult != null && hitResult.getEntity() instanceof LivingEntity target) {
                // Get current Slowness level (if any) BEFORE applying it
                int slownessLevel = 0;
                if (target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                    slownessLevel = target.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getAmplifier() + 1;
                }

                float bonusMultiplier = 1.0f + 0.2f * slownessLevel;
                float finalDamage = this.damage * bonusMultiplier;

                DamageSources sources = serverLevel.damageSources();
                DamageSource source = sources.playerAttack(player);
                applyGunDamage(target, source, finalDamage);

                // Now apply Slowness (level 2 for 5 seconds)
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
            }

            spawnParticles(serverLevel, player, range);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.GUNSHOT.get(), SoundSource.PLAYERS, 0.5f, 1f);

        player.getCooldowns().addCooldown(this, cooldownTicks);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    protected void spawnParticles(ServerLevel level, Player player, double range) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle();

        for (int i = 0; i <= 10; i++) {
            double progress = i / 10.0;
            Vec3 point = eyePos.add(look.scale(range * progress));
            level.sendParticles(ParticleTypes.SNOWFLAKE, point.x, point.y, point.z, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.literal("§6Shatter Shells"));
        tooltip.add(Component.literal("§7Fires a shell that slows targets."));
        tooltip.add(Component.literal("§7Gains 20% bonus damage per level of Slowness on the target."));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("§7When held:"));
        tooltip.add(Component.literal("§2 " + damage + "§2 Damage"));
        tooltip.add(Component.literal("§2 " + range + "§2 Range"));
        tooltip.add(Component.literal("§2 " + Mth.floor(20f / cooldownTicks) + "§2 Fire Rate"));
    }

    @Override
    protected boolean shouldShowBaseTooltip() {
        return false;
    }
}
