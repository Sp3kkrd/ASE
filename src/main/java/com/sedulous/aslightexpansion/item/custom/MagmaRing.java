package com.sedulous.aslightexpansion.item.custom;

import com.sedulous.aslightexpansion.ASlightExpansion;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ASlightExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagmaRing extends Item {

    private static final UUID DAMAGE_BOOST_ID = UUID.randomUUID();
    private static final double DAMAGE_BOOST_MULTIPLIER = 0.25;

    public MagmaRing(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        Style goldStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700));
        Style darkGreyStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xA9A9A9));

        tooltip.add(Component.literal("Flaming Embrace:").setStyle(goldStyle));
        tooltip.add(Component.literal("While the ring is in your offhand, you gain Fire Resistance.").setStyle(darkGreyStyle));
        tooltip.add(Component.literal("While you are on fire, you deal 25% more damage.").setStyle(darkGreyStyle));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            if (player != null && !player.level().isClientSide()) {
                ItemStack offhand = player.getOffhandItem();
                if (offhand.getItem() instanceof MagmaRing) {
                    // Give Fire Resistance if not already applied
                    if (!player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, true, false));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        if (!player.level().isClientSide()) {
            ItemStack offhand = player.getOffhandItem();
            if (offhand.getItem() instanceof MagmaRing && player.isOnFire()) {
                // Boost outgoing damage by 25% if holding the ring and burning
                float originalDamage = event.getAmount();
                float boostedDamage = originalDamage * (1.0f + (float) DAMAGE_BOOST_MULTIPLIER);
                event.setAmount(boostedDamage);
            }
        }
    }
}
