package net.sodiumzh.nmd.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nmd.entity.BossEventStatics;

public class BossBarHidingTagItem extends Item
{

	public BossBarHidingTagItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) 
	{
		ItemStack stack1 = player.getItemInHand(hand);
		if (!stack1.is(this))
			return InteractionResult.PASS;
		if (!(target instanceof Mob mob))
			return InteractionResult.PASS;
		if (target.level().isClientSide())
			return InteractionResult.sidedSuccess(target.level().isClientSide());
		if (BossEventStatics.getLinkedBossEvent(mob).isEmpty())
		{
			player.sendSystemMessage(Component.translatable("info.natriummobdisplay.no_boss_bar"));
		}
		else if (BossEventStatics.isBossDisabled(mob))
		{
			player.sendSystemMessage(Component.translatable("info.natriummobdisplay.boss_bar_already_disabled"));
		}
		else 
		{
			player.sendSystemMessage(Component.translatable("info.natriummobdisplay.boss_bar_disabled"));
			BossEventStatics.setBossDisabled(mob, true);
			BossEventStatics.updateLinkedBossEvent(mob);
			if (!player.isCreative() && !player.isSpectator())
				stack1.shrink(1);
		}
		return InteractionResult.sidedSuccess(target.level().isClientSide());
	}

}
