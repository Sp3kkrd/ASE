package com.sedulous.aslightexpansion.item.custom;

import com.sedulous.aslightexpansion.ASlightExpansion;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = ASlightExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TheConduit extends SwordItem {
    private static final java.util.Map<UUID, Boolean> wasHoldingConduit = new java.util.HashMap<>();

    private static final int BOOST_DURATION_TICKS = 200; // 10 seconds * 20 ticks per second
    private static final Map<UUID, Integer> lightningBoosts = new HashMap<>();

    public TheConduit(Tier pTier, Properties pProperties) {
        super(pTier, pProperties.rarity(Rarity.EPIC));
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        tooltip.add(Component.literal("§6Rampant Conduction:"));
        tooltip.add(Component.literal("§7On hit, 25% chance to summon lightning on the target."));
        tooltip.add(Component.literal("§7Kills increase the chance to 50% for 10 seconds."));
    }


    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean success = super.hurtEnemy(stack, target, attacker);
        if (!attacker.level().isClientSide && success) {
            handleRampantConduction(target, attacker);
        }
        return success;
    }

    private void handleRampantConduction(LivingEntity target, LivingEntity attacker) {
        Level level = attacker.level();
        UUID attackerId = attacker.getUUID();
        float chance = 0.25f;

        if (lightningBoosts.containsKey(attackerId) && lightningBoosts.get(attackerId) > 0) {
            chance = 0.5f;
        }

        if (level.random.nextFloat() < chance) {
            summonLightning(level, target.getX(), target.getY(), target.getZ());
        }
    }

    private void summonLightning(Level level, double x, double y, double z) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        if (lightning != null) {
            lightning.moveTo(x, y, z);
            level.addFreshEntity(lightning);
            level.gameEvent(lightning, GameEvent.LIGHTNING_STRIKE, new Vec3(x, y, z));
        }
    }

    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity killer && event.getEntity() instanceof LivingEntity) {
            ItemStack weapon = killer.getMainHandItem();
            if (weapon.getItem() instanceof TheConduit) {
                lightningBoosts.put(killer.getUUID(), BOOST_DURATION_TICKS);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.END) {
            Player player = event.player;
            if (player != null) {
                UUID playerUUID = player.getUUID();
                boolean hasBoost = lightningBoosts.containsKey(playerUUID) && lightningBoosts.get(playerUUID) > 0;

                // Countdown boost
                if (hasBoost) {
                    int remainingTicks = lightningBoosts.get(playerUUID);
                    if (remainingTicks > 0) {
                        lightningBoosts.put(playerUUID, remainingTicks - 1);
                    } else {
                        lightningBoosts.remove(playerUUID);
                        hasBoost = false;
                    }
                }

                ItemStack heldItem = player.getMainHandItem();
                boolean isHoldingConduit = heldItem.getItem() instanceof TheConduit;
                boolean wasHolding = wasHoldingConduit.getOrDefault(playerUUID, false);

                // Display action bar only if actively holding and boosted
                if (hasBoost && isHoldingConduit) {
                    player.displayClientMessage(Component.literal("§b⚡ Rampant Conduction Active ⚡"), true);
                }

                // If player was holding before but now not holding, clear the action bar
                if (wasHolding && !isHoldingConduit) {
                    player.displayClientMessage(Component.literal(""), true);
                }

                // Update for next tick
                wasHoldingConduit.put(playerUUID, isHoldingConduit);
            }
        }
    }
}