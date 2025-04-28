package com.sedulous.aslightexpansion.item.custom;

import com.sedulous.aslightexpansion.FunMod;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = FunMod.MOD_ID)
public class FerocityTalisman extends Item {

    public FerocityTalisman(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        Style goldStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700));
        Style darkGreyStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xA9A9A9));

        tooltip.add(Component.literal("Sharpened Fangs:").setStyle(goldStyle));
        tooltip.add(Component.literal("While in your offhand, your tamed wolves gain Strength and Speed.").setStyle(darkGreyStyle));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END || event.player.level().isClientSide()) {
            return;
        }

        Player player = event.player;
        ItemStack offhand = player.getOffhandItem();

        if (offhand.getItem() instanceof FerocityTalisman) {
            // Find nearby tamed wolves owned by the player
            List<Wolf> wolves = player.level().getEntitiesOfClass(Wolf.class, player.getBoundingBox().inflate(20),
                    wolf -> wolf.isOwnedBy(player));

            for (Wolf wolf : wolves) {
                // Give Strength
                wolf.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 0, true, true));
                // Give Speed
                wolf.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, true, true));
            }
        }
    }
}