package net.sodiumzh.mobnaming.network;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import net.sodiumzh.mobnaming.MobNamingUtilities;
import net.sodiumzh.mobnaming.entity.capability.CBossEventHandler;
import net.sodiumzh.mobnaming.util.Utils;

@Mod.EventBusSubscriber(modid = MobNamingUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MNUNetworkChannel {
    public static SimpleChannel CHANNEL;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {

        CHANNEL = Utils.newChannel(MobNamingUtilities.MOD_ID, "notaboss_channel", VERSION);
        Utils.registerDefaultClientGamePacket(nextID(), CHANNEL, CBossEventHandler.ClientboundSyncPacket.class);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() ->
        {
            registerMessage();
        });
    }
}
