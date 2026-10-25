package net.sodiumzh.nmd;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumzh.nmd.entity.BossInfoReferences;
import net.sodiumzh.nmd.eventlisteners.EntityEventListeners;
import net.sodiumzh.nmd.eventlisteners.SetupEventListeners;
import net.sodiumzh.nmd.mixins.NMDServerBossEventMixin;
import net.sodiumzh.nmd.registry.NMDItems;

/**
 * Natrium Mob Naming Utilities provides utility items for mob name and hp bar display.
 * <p>As {@link ServerBossEvent}s (the handler of a boss bar)
 * don't contain the mob reference, the corresponding needs to be referred by context.
 * This referring is mainly conducted in {@link EntityEventListeners}, {@link SetupEventListeners}
 * and {@link NMDServerBossEventMixin}. Reference results are stored in {@link BossInfoReferences} for {@code ServerBossEvent}s
 * and in Data/Syncher components for {@code Mob}s.
 */
@Mod(NatriumMobDisplay.MOD_ID)
public class NatriumMobDisplay
{
    public static final String MOD_ID = "natriummobdisplay";
    
    public NatriumMobDisplay()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        NMDItems.ITEMS.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
        BossInfoReferences.init();
    }
}
