package com.sedulous.aslightexpansion.item.custom;

import com.google.common.collect.ImmutableMap;
import com.sedulous.aslightexpansion.item.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class NatureArmorItem extends ArmorItem {
    private static final Map<Holder<ArmorMaterial>, List<MobEffectInstance>> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<Holder<ArmorMaterial>, List<MobEffectInstance>>())
                    .put(ModArmorMaterials.NATURE_MATERIAL,
                            List.of(new MobEffectInstance(MobEffects.REGENERATION, 200, 1, false, false),
                                    new MobEffectInstance(MobEffects.SATURATION, 200, 1, false, false)))
                    .build();

    private float lastKnownHealth = -1;

    public NatureArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        if (!level.isClientSide() && hasFullSuitOfArmorOn(player)) {
            evaluateArmorEffects(player);
            healNearbyAlliesOnHeal(player, level);
        }
    }

    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<Holder<ArmorMaterial>, List<MobEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            Holder<ArmorMaterial> mapArmorMaterial = entry.getKey();
            List<MobEffectInstance> mapEffect = entry.getValue();

            if (hasPlayerCorrectArmorOn(mapArmorMaterial, player)) {
                addEffectToPlayer(player, mapEffect);
            }
        }
    }

    private void healNearbyAlliesOnHeal(Player player, Level level) {
        float currentHealth = player.getHealth();

        if (lastKnownHealth > 0 && currentHealth > lastKnownHealth) {
            float healAmount = currentHealth - lastKnownHealth;

            for (Entity entity : level.getEntities(player, player.getBoundingBox().inflate(10), e ->
                    e instanceof Player || (e instanceof TamableAnimal tamable && tamable.isTame() && tamable.isOwnedBy(player)))) {
                if (entity instanceof LivingEntity living && living.isAlive()) {
                    living.heal(healAmount / 2); // Heal allies for half of what the player healed
                }
            }
        }

        lastKnownHealth = currentHealth;
    }

    private void addEffectToPlayer(Player player, List<MobEffectInstance> mapEffect) {
        boolean hasPlayerEffect = mapEffect.stream().allMatch(effect -> player.hasEffect(effect.getEffect()));

        if (!hasPlayerEffect) {
            for (MobEffectInstance effect : mapEffect) {
                player.addEffect(new MobEffectInstance(effect.getEffect(),
                        effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
            }
        }
    }

    private boolean hasPlayerCorrectArmorOn(Holder<ArmorMaterial> mapArmorMaterial, Player player) {
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (!(armorStack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }

        ArmorItem boots = ((ArmorItem) player.getInventory().getArmor(0).getItem());
        ArmorItem leggings = ((ArmorItem) player.getInventory().getArmor(1).getItem());
        ArmorItem chestplate = ((ArmorItem) player.getInventory().getArmor(2).getItem());
        ArmorItem helmet = ((ArmorItem) player.getInventory().getArmor(3).getItem());

        return boots.getMaterial() == mapArmorMaterial && leggings.getMaterial() == mapArmorMaterial
                && chestplate.getMaterial() == mapArmorMaterial && helmet.getMaterial() == mapArmorMaterial;
    }

    private boolean hasFullSuitOfArmorOn(Player player) {
        ItemStack boots = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack helmet = player.getInventory().getArmor(3);

        return !boots.isEmpty() && !leggings.isEmpty() && !chestplate.isEmpty() && !helmet.isEmpty();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("§7Set Bonus:"));
        tooltip.add(Component.literal("§6Verdant Embrace"));
        tooltip.add(Component.literal("§7Grants Regeneration and Saturation"));
        tooltip.add(Component.literal("§7Heals nearby allies when you are healed"));
    }
}
