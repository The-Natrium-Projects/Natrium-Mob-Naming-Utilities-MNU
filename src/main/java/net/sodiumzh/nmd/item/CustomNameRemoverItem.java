package net.sodiumzh.nmd.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumzh.nfu.item.NFUItem;

public class CustomNameRemoverItem extends NFUItem {
    public CustomNameRemoverItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof Mob mob && mob.hasCustomName()) {
            target.setCustomName(null);
            if (!player.level().isClientSide)
                player.getItemInHand(hand).shrink(1);
            return InteractionResult.sidedSuccess(target.level().isClientSide());
        }
        return InteractionResult.PASS;
    }

}
