package net.sodiumzh.nmd.registry;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nmd.NatriumMobDisplay;
import net.sodiumzh.nmd.entity.BossEventStatics;
import net.sodiumzh.nfu.entity.component.EntityComponentFinalizeSetupEvent;
import net.sodiumzh.nfu.entity.component.EntityComponentTypes;
import net.sodiumzh.nfu.network.NFUDataSerializers;

@Mod.EventBusSubscriber(modid = NatriumMobDisplay.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NMDEntityComponents
{

	@SubscribeEvent
	public static void onComponentFinishSetup(EntityComponentFinalizeSetupEvent event) {
		event.getComponentManager().getSubComponentByPath(EntityComponentTypes.ACCESSOR_DEFAULT_SYNCHER)
			.orElseThrow().createSynchedData(BossEventStatics.KEY_BOSS_DISABLED, NFUDataSerializers.BOOLEAN.get(), false, true);
	}

}
