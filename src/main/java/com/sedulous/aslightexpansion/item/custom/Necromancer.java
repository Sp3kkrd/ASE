package com.sedulous.aslightexpansion.item.custom;

import com.sedulous.aslightexpansion.entity.custom.HomingWitherSkull;
import com.sedulous.aslightexpansion.item.ModItems;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Necromancer extends GunItem {
    private int hitCounter = 0;

    public Necromancer(float damage, float range, int fireRateTicks, Properties properties) {
        super(damage, range, fireRateTicks, properties);
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
                DamageSources sources = serverLevel.damageSources();
                DamageSource source = sources.playerAttack(player);
                applyGunDamage(target, source, damage);

                hitCounter++;

                if (hitCounter >= 5) {
                    hitCounter = 0;

                    List<Mob> mobs = serverLevel.getEntitiesOfClass(Mob.class, new AABB(player.blockPosition()).inflate(30),
                            mob -> mob.isAlive() && !mob.isAlliedTo(player) && !mob.isPassenger() && !mob.isVehicle());

                    if (!mobs.isEmpty()) {
                        Mob nearestMob = mobs.get(0);
                        double nearestDistance = player.distanceToSqr(nearestMob);

                        for (Mob mob : mobs) {
                            double distance = player.distanceToSqr(mob);
                            if (distance < nearestDistance) {
                                nearestDistance = distance;
                                nearestMob = mob;
                            }
                        }

                        spawnWitherSkull(serverLevel, player, nearestMob);
                    }
                }
            }

            spawnParticles(serverLevel, player, range);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.3f, 1.0f);

        player.getCooldowns().addCooldown(this, cooldownTicks);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private void spawnWitherSkull(ServerLevel level, Player player, LivingEntity target) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 right = player.getLookAngle().cross(new Vec3(0, 1, 0)).normalize().scale(0.5);
        Vec3 spawnPos = eyePos.add(right).add(0, 0.2, 0); // Slightly above and to the side

        HomingWitherSkull skull = new HomingWitherSkull(level, player);
        skull.setTarget(target);
        skull.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, player.getYRot(), player.getXRot());
        level.addFreshEntity(skull);

        // Spawn some particles where the skull appears
        for (int i = 0; i < 10; i++) {
            level.sendParticles(ParticleTypes.SOUL, spawnPos.x, spawnPos.y, spawnPos.z,
                    1, 0.2, 0.2, 0.2, 0.01);
            level.sendParticles(ParticleTypes.SMOKE, spawnPos.x, spawnPos.y, spawnPos.z,
                    1, 0.1, 0.1, 0.1, 0.02);
        }
    }

    @Override
    protected void spawnParticles(ServerLevel level, Player player, double range) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle();

        for (int i = 0; i <= 10; i++) {
            double progress = i / 10.0;
            Vec3 point = eyePos.add(look.scale(range * progress));
            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, point.x, point.y, point.z, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.literal("§6Nether Invictus"));
        pTooltipComponents.add(Component.literal("§7Every 5 hits, spawns a homing wither skull that targets the nearest hostile mob within 30 blocks, dealing 10 damage and applying Wither for 5 seconds."));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.literal("§7When held:"));
        pTooltipComponents.add(Component.literal("§2 " + damage + "§2 Damage"));
        pTooltipComponents.add(Component.literal("§2 " + range + "§2 Range"));
        pTooltipComponents.add(Component.literal("§2 " + Mth.floor(20f / cooldownTicks) + "§2 Fire Rate"));
    }

    @Override
    protected boolean shouldShowBaseTooltip() {
        return false;
    }
}
