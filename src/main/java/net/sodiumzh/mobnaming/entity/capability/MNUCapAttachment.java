package net.sodiumzh.mobnaming.entity.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.mobnaming.MobNamingUtilities;

@Mod.EventBusSubscriber(modid = MobNamingUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MNUCapAttachment
{
	public static final String KEY_BOSS_EVENT_HANDLER = "mnu_boss_event_handler";
	
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof Mob mob)
			event.addCapability(new ResourceLocation(MobNamingUtilities.MOD_ID, KEY_BOSS_EVENT_HANDLER), new CBossEventHandler.Prvd(mob));
	}
}
