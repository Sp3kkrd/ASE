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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Abaddon extends GunItem {

    public Abaddon(float damage, float range, int fireRateTicks, Properties properties) {
        super(damage, range, fireRateTicks, properties);
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

            // Proceed with firing logic
            if (level instanceof ServerLevel serverLevel) {
                EntityHitResult hitResult = raycastEntities(serverLevel, player, range);

                if (hitResult != null && hitResult.getEntity() instanceof LivingEntity target) {
                    DamageSources sources = serverLevel.damageSources();
                    DamageSource source = sources.playerAttack(player);
                    boolean wasBurning = target.isOnFire();

                    target.setRemainingFireTicks(100); // Set target on fire
                    applyGunDamage(target, source, damage);

                    // If target was on fire and just died, spread fire to nearby mobs
                    if (wasBurning && !target.isAlive()) {
                        igniteNearbyEnemies(serverLevel, target.position(), player);
                    }
                }

                spawnParticles(serverLevel, player, range);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.GUNSHOT.get(), SoundSource.PLAYERS, 0.2f, 0.9f);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.5f, 1.0f);

            player.getCooldowns().addCooldown(this, cooldownTicks);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private void igniteNearbyEnemies(ServerLevel level, Vec3 position, Player player) {
        AABB area = new AABB(position.subtract(10, 10, 10), position.add(10, 10, 10));
        level.getEntitiesOfClass(LivingEntity.class, area, e -> !(e instanceof Player) && !(e instanceof Villager)).forEach(entity -> {
            entity.setRemainingFireTicks(100); // Ignites nearby enemies for 5 seconds
        });
    }
    @Override
    protected void spawnParticles(ServerLevel level, Player player, double range) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle();

        for (int i = 0; i <= 10; i++) {
            double progress = i / 10.0;
            Vec3 point = eyePos.add(look.scale(range * progress));
            level.sendParticles(ParticleTypes.FLAME, point.x, point.y, point.z, 1, 0, 0, 0, 0);
        }
    }
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.literal("§6Wildfire"));
        pTooltipComponents.add(Component.literal("§7Mobs hit by this weapon are set on fire."));
        pTooltipComponents.add(Component.literal("§7Hitting a flaming mob spreads the flames to nearby mobs. "));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.literal("§7When held:"));
        pTooltipComponents.add(Component.literal("§2 " + damage + "§2 Damage"));
        pTooltipComponents.add(Component.literal("§2 " + range + "§2 Range"));
        pTooltipComponents.add(Component.literal("§2 " + Mth.floor(20f / cooldownTicks) + "§2 Fire Rate"));
    }
}
