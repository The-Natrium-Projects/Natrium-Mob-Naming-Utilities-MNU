package net.sodiumzh.naming.eventlisteners;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.naming.MobNamingUtilities;
import net.sodiumzh.naming.registry.MNUItems;

@Mod.EventBusSubscriber(modid = MobNamingUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEventListeners
{
	
	@SubscribeEvent
	public static void onCreativeModTabBuild(BuildCreativeModeTabContentsEvent event)
	{
		if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES))
		{
			event.accept(MNUItems.BOSS_BAR_HIDING_TAG::get);
			event.accept(MNUItems.BOSS_BAR_SHOWING_TAG::get);
			event.accept(MNUItems.NAME_REMOVER::get);
		}
	}
}
