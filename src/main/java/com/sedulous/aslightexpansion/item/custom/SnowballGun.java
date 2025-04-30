package com.sedulous.aslightexpansion.item.custom;

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

public class SnowballGun extends Item {
    public SnowballGun(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        pTooltipComponents.add(Component.literal("§2Damage: 3"));
        pTooltipComponents.add(Component.literal("§2Fire Rate: 4"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand); // Start "using" to trigger bow pose immediately

        if (!level.isClientSide) {
            SnowballGunProjectile projectile = new SnowballGunProjectile(level, player);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(projectile);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.PLAYERS,
                    1.0F, 1.0F + (level.random.nextFloat() - 0.5F) * 0.2F);
        }

        player.getCooldowns().addCooldown(this, 5); // 4 shots per second
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
