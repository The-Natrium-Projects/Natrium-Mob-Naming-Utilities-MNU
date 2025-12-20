package net.sodiumzh.mobnaming.registry;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.mobnaming.MobNamingUtilities;
import net.sodiumzh.mobnaming.entity.capability.CBossEventHandler;

@Mod.EventBusSubscriber(modid = MobNamingUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MNUCaps
{
	public static final Capability<CBossEventHandler> BOSS_EVENT_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event)
	{
		event.register(CBossEventHandler.class);
	}
}
