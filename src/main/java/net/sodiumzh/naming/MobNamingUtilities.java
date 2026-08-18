package net.sodiumzh.naming;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumzh.naming.entity.BossInfoReferences;
import net.sodiumzh.naming.eventlisteners.EntityEventListeners;
import net.sodiumzh.naming.eventlisteners.SetupEventListeners;
import net.sodiumzh.naming.mixins.MNUServerBossEventMixin;
import net.sodiumzh.naming.registry.MNUItems;

/**
 * Natrium Mob Naming Utilities provides utility items for mob name and hp bar display.
 * <p>As {@link ServerBossEvent}s (the handler of a boss bar)
 * don't contain the mob reference, the corresponding needs to be referred by context.
 * This referring is mainly conducted in {@link EntityEventListeners}, {@link SetupEventListeners}
 * and {@link MNUServerBossEventMixin}. Reference results are stored in {@link BossInfoReferences} for {@code ServerBossEvent}s
 * and in Data/Syncher components for {@code Mob}s.
 */
@Mod(MobNamingUtilities.MOD_ID)
public class MobNamingUtilities
{
    public static final String MOD_ID = "natriumnaming";
    
    public MobNamingUtilities()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MNUItems.ITEMS.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
        BossInfoReferences.init();
    }
}
