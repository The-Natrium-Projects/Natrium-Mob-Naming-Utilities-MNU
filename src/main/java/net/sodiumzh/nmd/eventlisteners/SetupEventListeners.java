package net.sodiumzh.nmd.eventlisteners;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nmd.NatriumMobDisplay;
import net.sodiumzh.nmd.registry.NMDItems;

@Mod.EventBusSubscriber(modid = NatriumMobDisplay.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEventListeners
{
	
	@SubscribeEvent
	public static void onCreativeModTabBuild(BuildCreativeModeTabContentsEvent event)
	{
		if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES))
		{
			event.accept(NMDItems.BOSS_BAR_HIDING_TAG::get);
			event.accept(NMDItems.BOSS_BAR_SHOWING_TAG::get);
			event.accept(NMDItems.NAME_REMOVER::get);
		}
	}
}
