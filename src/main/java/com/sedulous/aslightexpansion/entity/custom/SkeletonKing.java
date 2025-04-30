package com.sedulous.aslightexpansion.entity.custom;

import com.sedulous.aslightexpansion.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SkeletonKing extends Skeleton {

    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.literal("Skeleton King"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

    private boolean guardsSummoned = false;
    private int chargeCooldown = 0;
    private boolean isCharging = false;
    private boolean isTelegraphing = false;
    private int telegraphTicks = 0;
    private int chargeTicks = 0;
    private Vec3 chargeTargetPos = null;

    private static final int TELEGRAPH_DURATION = 40; // 2 seconds at 20 ticks/sec
    private static final int CHARGE_DURATION = 20; // about 1 second
    private static final int CHARGE_COOLDOWN_TICKS = 200; // 10 seconds cooldown
    private static final double CHARGE_SPEED = 1.2D;
    private static final double CHARGE_DAMAGE = 10.0D;

    public SkeletonKing(EntityType<? extends SkeletonKing> type, Level level) {
        super(type, level);
        this.xpReward = 50;
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }

        if (this.getHealth() <= this.getMaxHealth() / 2 && !guardsSummoned) {
            summonSkeletonGuards();
            guardsSummoned = true;
        }

        if (chargeCooldown > 0) {
            chargeCooldown--;
        }

        if (isTelegraphing) {
            telegraphTicks++;
            this.getNavigation().stop(); // Stop moving
            if (this.getTarget() != null) {
                this.getLookControl().setLookAt(this.getTarget(), 30.0F, 30.0F);
            }
            // Angry particles
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, this.getX(), this.getY() + 2, this.getZ(), 2, 0.5, 0.5, 0.5, 0.0);
            }
            if (telegraphTicks >= TELEGRAPH_DURATION) {
                startCharge();
            }
            return;
        }

        if (isCharging) {
            chargeTicks++;

            if (chargeTargetPos != null) {
                Vec3 direction = chargeTargetPos.subtract(this.position()).normalize();
                Vec3 motion = direction.scale(CHARGE_SPEED);
                this.setDeltaMovement(motion);
                this.setYRot((float)(Mth.atan2(direction.z, direction.x) * (180F / Math.PI)) - 90.0F);
                this.setYHeadRot(this.getYRot());
                this.yBodyRot = this.getYRot();
            }

            if (chargeTicks >= CHARGE_DURATION) {
                stopCharge();
            } else {
                checkChargeCollision();
            }
            return;
        }

        if (chargeCooldown <= 0 && this.getTarget() != null && this.distanceToSqr(this.getTarget()) <= 100) {
            startTelegraph();
        }
    }

    private void startTelegraph() {
        isTelegraphing = true;
        telegraphTicks = 0;
    }

    private void startCharge() {
        isTelegraphing = false;
        isCharging = true;
        chargeTicks = 0;
        if (this.getTarget() != null) {
            chargeTargetPos = this.getTarget().position();
        } else {
            chargeTargetPos = null;
        }
    }

    private void stopCharge() {
        isCharging = false;
        chargeCooldown = CHARGE_COOLDOWN_TICKS;
        this.setDeltaMovement(Vec3.ZERO);
    }

    private void checkChargeCollision() {
        AABB bounds = this.getBoundingBox().inflate(1.0D);
        List<Player> players = this.level().getEntitiesOfClass(Player.class, bounds);

        for (Player player : players) {
            if (player.isAlive()) {
                player.hurt(this.damageSources().mobAttack(this), (float) CHARGE_DAMAGE);
                stopCharge();
                break;
            }
        }
    }

    private void summonSkeletonGuards() {
        if (!this.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                Skeleton guard = EntityType.SKELETON.create(this.level());
                if (guard != null) {
                    guard.moveTo(this.getX() + Mth.nextDouble(this.random, -3.0D, 3.0D),
                            this.getY(),
                            this.getZ() + Mth.nextDouble(this.random, -3.0D, 3.0D),
                            this.getYRot(),
                            0.0F);
                    this.level().addFreshEntity(guard);
                }
            }
        }
    }

    private boolean hasAliveGuards() {
        return this.level().getEntitiesOfClass(Skeleton.class, this.getBoundingBox().inflate(16.0D), e -> e != this && e.isAlive()).size() > 0;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.KINGS_CLAYMORE.get()));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.KINGS_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.KINGS_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.KINGS_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.KINGS_BOOTS.get()));
    }
}
