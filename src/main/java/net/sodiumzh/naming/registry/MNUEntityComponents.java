package net.sodiumzh.naming.registry;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.naming.MobNamingUtilities;
import net.sodiumzh.naming.entity.BossEventStatics;
import net.sodiumzh.nfu.entity.component.EntityComponentFinalizeSetupEvent;
import net.sodiumzh.nfu.entity.component.EntityComponentSetupEvent;
import net.sodiumzh.nfu.entity.component.EntityComponentType;
import net.sodiumzh.nfu.entity.component.EntityComponentTypes;
import net.sodiumzh.nfu.network.NFUDataSerializers;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

@Mod.EventBusSubscriber(modid = MobNamingUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MNUEntityComponents
{

	@SubscribeEvent
	public static void onComponentFinishSetup(EntityComponentFinalizeSetupEvent event) {
		event.getComponentManager().getSubComponentByPath(EntityComponentTypes.ACCESSOR_DEFAULT_SYNCHER)
			.orElseThrow().createSynchedData(BossEventStatics.KEY_BOSS_DISABLED, NFUDataSerializers.BOOLEAN, false, true);
	}

}
