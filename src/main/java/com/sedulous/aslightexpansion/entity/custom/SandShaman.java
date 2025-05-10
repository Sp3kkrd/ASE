package com.sedulous.aslightexpansion.entity.custom;

import com.sedulous.aslightexpansion.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SandShaman extends Human {
    private int fangCooldown = 0;
    private static final int FANG_COOLDOWN_TICKS = 60; // 5 seconds
    private static final int FANG_RANGE = 15;

    public SandShaman(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.SAND_SCOURGE_STAFF.get()));

        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        this.setDropChance(EquipmentSlot.LEGS, 0.0F);
        this.setDropChance(EquipmentSlot.FEET, 0.0F);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FangCastGoal(this, 1.5D, FANG_COOLDOWN_TICKS, FANG_RANGE));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }


    public static class FangCastGoal extends Goal {
        private final SandShaman shaman;
        private final double speedModifier;
        private final int castCooldownTicks;
        private final float maxRange;
        private int cooldownTime;
        private int seeTime;

        public FangCastGoal(SandShaman shaman, double speedModifier, int castCooldownTicks, float maxRange) {
            this.shaman = shaman;
            this.speedModifier = speedModifier;
            this.castCooldownTicks = castCooldownTicks;
            this.maxRange = maxRange;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.shaman.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void start() {
            this.cooldownTime = 0;
            this.seeTime = 0;

        }

        @Override
        public void tick() {
            LivingEntity target = this.shaman.getTarget();
            if (target == null) return;

            double distanceSq = this.shaman.distanceToSqr(target);
            boolean canSee = this.shaman.getSensing().hasLineOfSight(target);

            if (canSee) {
                seeTime++;
            } else {
                seeTime = 0;
            }

            // Movement: back up if too close, move forward if too far
            if (distanceSq < 12D) {
                this.shaman.getNavigation().moveTo(this.shaman.getX() - (target.getX() - this.shaman.getX()),
                        this.shaman.getY(),
                        this.shaman.getZ() - (target.getZ() - this.shaman.getZ()),
                        this.speedModifier);
            } else if (distanceSq > maxRange * maxRange) {
                this.shaman.getNavigation().moveTo(target, this.speedModifier);
            } else {
                this.shaman.getNavigation().stop();
            }

            this.shaman.getLookControl().setLookAt(target, 30.0F, 30.0F);

            // Cast when ready
            if (--cooldownTime <= 0 && canSee && seeTime > 10) {
                castFangs(target);
                cooldownTime = castCooldownTicks;
            }
        }

        private void castFangs(LivingEntity target) {
            if (!(shaman.level() instanceof ServerLevel serverLevel)) return;

            // Play custom animation (if using animation states)
            shaman.attackAnimationState.start(shaman.tickCount);


            // Central fang
            EvokerFangs mainFang = new EvokerFangs(serverLevel, target.getX(), target.getY(), target.getZ(), 0, 0, this.shaman);
            serverLevel.addFreshEntity(mainFang);

            // Spawn additional fangs in a circle
            for (int i = 0; i < 8; i++) {
                double angle = i * Math.PI / 4;
                double dx = Math.cos(angle) * 1.5;
                double dz = Math.sin(angle) * 1.5;
                double x = target.getX() + dx;
                double z = target.getZ() + dz;

                EvokerFangs fang = new EvokerFangs(serverLevel, x, target.getY(), z, 0, 10 + i * 2, this.shaman); // Delay for effect
                serverLevel.addFreshEntity(fang);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Human.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
}
