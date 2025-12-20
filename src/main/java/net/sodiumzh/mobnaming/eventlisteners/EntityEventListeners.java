package net.sodiumzh.mobnaming.eventlisteners;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.mobnaming.BossInfoReferences;
import net.sodiumzh.mobnaming.MobNamingUtilities;
import net.sodiumzh.mobnaming.entity.capability.IBossEventMobAccessor;
import net.sodiumzh.mobnaming.registry.MNUCaps;

@Mod.EventBusSubscriber(modid = MobNamingUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventListeners
{
	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event)
	{
		// On each mob's first create, find if there's a boss info field in the mob class
		if (event.getLevel().isClientSide())
			return;
		if (!(event.getEntity() instanceof Mob))
			return;
		Mob mob = (Mob)(event.getEntity());
		BossInfoReferences.recordBossEventAccessor(mob);
		
		// Find the field
		BossInfoReferences.getLinkedBossEvent(mob).ifPresent(sbe -> {
			if (sbe instanceof IBossEventMobAccessor accessor)
				accessor.setLinkedMob(mob);
			mob.getCapability(MNUCaps.BOSS_EVENT_HANDLER).ifPresent(c -> c.setLinkedBossEvent(sbe));
		});
	}
	
	@SubscribeEvent
	public static void onLivingTick(LivingTickEvent event)
	{
		if (!event.getEntity().level().isClientSide)
		{
			event.getEntity().getCapability(MNUCaps.BOSS_EVENT_HANDLER).ifPresent(c -> {
				if (c.isBossDisabled())
					c.updateLinkedBossEvent();
				if (event.getEntity().tickCount % 5 == 1)
					c.syncToClient();
			});
		}
	}
	
}
