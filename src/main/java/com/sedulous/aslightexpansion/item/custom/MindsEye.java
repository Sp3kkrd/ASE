package com.sedulous.aslightexpansion.item.custom;

import com.sedulous.aslightexpansion.ASlightExpansion;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = ASlightExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MindsEye extends SwordItem {

    private static final int COOLDOWN_TICKS = 200; // 10 seconds
    private static final double GLOW_RADIUS = 20.0;
    private static final Map<UUID, Integer> cooldowns = new HashMap<>();

    public MindsEye(Tier pTier, Properties pProperties) {
        super(pTier, pProperties.rarity(Rarity.EPIC));
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        Style goldStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700));
        Style darkGreyStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xA9A9A9));

        tooltip.add(Component.literal("Eye of the Unseen:").setStyle(goldStyle));
        tooltip.add(Component.literal("Press [Use Item] to afflict nearby mobs with Glowing for 20 seconds.").setStyle(darkGreyStyle));
        tooltip.add(Component.literal("Killing a glowing mob grants you Invisibility and Speed for 10 Seconds").setStyle(darkGreyStyle));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) { // Server only
            UUID playerId = player.getUUID();
            if (!isOnCooldown(playerId)) {
                applyEyeOfTheUnseen(player, level);
                setCooldown(playerId);
                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            } else {
                player.sendSystemMessage(Component.literal("§eEye of the Unseen is on cooldown!"));
            }
        }

        return InteractionResultHolder.success(stack);
    }

    private void applyEyeOfTheUnseen(Player player, Level level) {
        AABB area = new AABB(
                player.getX() - GLOW_RADIUS, player.getY() - GLOW_RADIUS, player.getZ() - GLOW_RADIUS,
                player.getX() + GLOW_RADIUS, player.getY() + GLOW_RADIUS, player.getZ() + GLOW_RADIUS
        );

        List<Entity> entities = level.getEntities(player, area, entity -> entity instanceof LivingEntity && entity != player);
        for (Entity entity : entities) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.GLOWING, 400, 0, false, false)); // 20s
        }
    }

    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        Entity source = event.getSource().getEntity();
        if (source instanceof Player player && event.getEntity() instanceof LivingEntity target) {
            if (target.hasEffect(MobEffects.GLOWING)) {
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0, false, false)); // 10s
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, false, false)); // 10s Speed II
            }
        }
    }

    private boolean isOnCooldown(UUID playerId) {
        return cooldowns.getOrDefault(playerId, 0) > 0;
    }

    private void setCooldown(UUID playerId) {
        cooldowns.put(playerId, COOLDOWN_TICKS);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.player.level().isClientSide()) {
            UUID playerId = event.player.getUUID();
            cooldowns.computeIfPresent(playerId, (uuid, ticks) -> (ticks > 1) ? ticks - 1 : null);
        }
    }
}
