package net.sodiumzh.nmd.eventlisteners;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nmd.entity.BossEventStatics;
import net.sodiumzh.nmd.entity.BossInfoReferences;
import net.sodiumzh.nmd.NatriumMobDisplay;
import net.sodiumzh.nmd.entity.IBossEventMobAccessor;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = NatriumMobDisplay.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventListeners
{
	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event)
	{
		// On each mob type's first instantiation, find if there's a boss info field in the mob class
		if (event.getLevel().isClientSide())
			return;
		if (!(event.getEntity() instanceof Mob))
			return;
		Mob mob = (Mob)(event.getEntity());
		try {
			BossInfoReferences.recordBossEventAccessor(mob);
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			BossInfoReferences.labelNoBossEvent((EntityType<? extends Mob>) mob.getType());
		}
		
		// Find the boss bar, and update references on both BossEvent instance and the mob
		@Nullable ServerBossEvent serverBossEvent = BossInfoReferences.getLinkedBossEvent(mob).orElse(null);
		if (serverBossEvent instanceof IBossEventMobAccessor accessor) {
			try {
				accessor.setLinkedMob(mob);
				BossEventStatics.setLinkedBossEvent(mob, serverBossEvent);
			} catch (RuntimeException ex) {
				ex.printStackTrace();
				accessor.setLinkedMob(null);
				BossEventStatics.setLinkedBossEvent(mob, null);
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingTick(LivingTickEvent event)
	{
		if (event.getEntity() instanceof Mob mob
			&& !event.getEntity().level().isClientSide
			&& event.getEntity().tickCount % 5 == 0)
		{
			if (BossEventStatics.isBossDisabled(mob))
				BossEventStatics.updateLinkedBossEvent(mob);
		}
	}
	
}
