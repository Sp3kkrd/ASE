package com.sedulous.aslightexpansion.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;


public class SandScourgeStaff extends Item {
    public SandScourgeStaff(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;

            List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class,
                    new AABB(player.blockPosition()).inflate(20),
                    entity -> !(entity instanceof Player)
                            && !(entity instanceof TamableAnimal tamable && tamable.isTame())
                            && !entity.getType().toShortString().toLowerCase().contains("villager")
                            && entity.isAlive()
            );

            for (LivingEntity target : targets) {
                BlockPos fangsPos = BlockPos.containing(target.getX(), target.getY(), target.getZ());

                // Check if the block below is solid to ensure fangs can spawn
                BlockState below = level.getBlockState(fangsPos.below());
                if (below.isSolidRender(level, fangsPos.below())) {
                    EvokerFangs fangs = new EvokerFangs(level, target.getX(), target.getY(), target.getZ(), 0, 0, player);
                    level.addFreshEntity(fangs);
                }
            }

            level.playSound(null, player.blockPosition(), SoundEvents.EVOKER_PREPARE_ATTACK, SoundSource.PLAYERS, 1.0f, 1.0f);
            player.getCooldowns().addCooldown(this, 100); // 5 seconds
        }


        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.empty());
        tooltip.add(Component.literal("§7[Use Item]"));
        tooltip.add(Component.literal("§2 Summon Evoker Fangs on enemies within 20 blocks"));
        tooltip.add(Component.literal("§2 5 Second Cooldown"));
    }

}
