package com.sedulous.aslightexpansion.item.custom;


import com.sedulous.aslightexpansion.ASlightExpansion;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ASlightExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PrimordialSnowglobe extends Item {

    private static final Map<LivingEntity, Integer> entitySlownessTimers = new HashMap<>();

    public PrimordialSnowglobe(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        Style goldStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFD700));
        Style darkGreyStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xA9A9A9));

        tooltip.add(Component.literal("Frigid Wasteland:").setStyle(goldStyle));
        tooltip.add(Component.literal("While in your offhand, generates a ring of snow particles.").setStyle(darkGreyStyle));
        tooltip.add(Component.literal("All hostile entities inside the ring are inflicted with an increasing slowness and are weakened.").setStyle(darkGreyStyle));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Level level = player.level();

        ItemStack offhandItem = player.getOffhandItem();
        if (!level.isClientSide && offhandItem.getItem() instanceof PrimordialSnowglobe) {
            double radius = 10.0;
            AABB area = new AABB(
                    player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                    player.getX() + radius, player.getY() + radius, player.getZ() + radius
            );

            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, entity -> {
                if (entity instanceof Player) return false;
                if (entity instanceof TamableAnimal tamable && tamable.isTame()) return false;
                if (entity instanceof Villager || entity instanceof WanderingTrader) return false;
                return true;
            });

            for (LivingEntity entity : entities) {
                int time = entitySlownessTimers.getOrDefault(entity, 0) + 1;
                entitySlownessTimers.put(entity, time);
                int amplifier = Math.min(time / 30, 3); //

                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, amplifier, false, true));
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1, false, false));
            }

            // Clean up entities no longer in range
            entitySlownessTimers.keySet().removeIf(entity -> !entities.contains(entity) || !entity.isAlive());
        } else if (offhandItem.getItem() instanceof PrimordialSnowglobe == false) {
            // Reset all timers if item is not held anymore
            entitySlownessTimers.clear();
        }
    }

    private static double spinOffset = 0; // Tracks the rotation over time

    private static void spawnSnowParticles(Player player, Level level) {
        if (!level.isClientSide()) return;

        double radius = 10.0;
        double y = player.getY() + 0.75;
        int density = 24; // How many particles around
        double spinSpeed = 0.02; // How fast to spin (radians per tick)

        spinOffset += spinSpeed; // Increment the spin each tick

        for (int i = 0; i < density; i++) {
            double angle = 2 * Math.PI * i / density + spinOffset;
            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);

            double x = player.getX() + xOffset;
            double z = player.getZ() + zOffset;

            level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0, 0.05, 0);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.level != null) {
            Player player = mc.player;
            ItemStack offhand = player.getOffhandItem();

            if (offhand.getItem() instanceof PrimordialSnowglobe) {
                spawnSnowParticles(player, player.level());
            }
        }
    }
}
