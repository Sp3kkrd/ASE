package com.sedulous.aslightexpansion.item.custom;

import com.sedulous.aslightexpansion.entity.custom.LargeSnowballGunProjectile;
import com.sedulous.aslightexpansion.entity.custom.SnowballGunProjectile;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random; // <-- import added

public class AutoSnowballCannon extends Item {
    private static final Random random = new Random(); // Proper static random

    public AutoSnowballCannon(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.literal("§6Packed Snowball:"));
        pTooltipComponents.add(Component.literal("§720% chance per shot to fire a larger snowball that deals double damage and inflicts a more potent slow."));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.literal("§2Damage: 3"));
        pTooltipComponents.add(Component.literal("§2Fire Rate: 8"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand); // Force bow animation

        if (!level.isClientSide) {
            if (random.nextFloat() < 1f) { // 20% chance
                LargeSnowballGunProjectile projectile = new LargeSnowballGunProjectile(level, player);
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.6F, 1.0F);
                level.addFreshEntity(projectile);
            } else {
                SnowballGunProjectile projectile = new SnowballGunProjectile(level, player);
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.6F, 1.0F);
                level.addFreshEntity(projectile);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.PLAYERS,
                    1.0F, 1.0F + (level.random.nextFloat() - 0.5F) * 0.2F);
        }

        player.getCooldowns().addCooldown(this, 1); // 8 shots per second
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}