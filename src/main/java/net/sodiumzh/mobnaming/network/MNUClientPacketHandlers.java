package net.sodiumzh.mobnaming.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumzh.mobnaming.entity.capability.CBossEventHandler;
import net.sodiumzh.mobnaming.registry.MNUCaps;

@OnlyIn(Dist.CLIENT)
public class MNUClientPacketHandlers
{
    public static void handleBossEventHandlerSync(CBossEventHandler.ClientboundSyncPacket packet, ClientGamePacketListener listener)
    {
        Minecraft mc = Minecraft.getInstance();
        PacketUtils.ensureRunningOnSameThread(packet, listener, mc);
        Entity e = mc.level.getEntity(packet.getEntityId());
        e.getCapability(MNUCaps.BOSS_EVENT_HANDLER).ifPresent(c -> c.setBossDisabled(packet.isBossDisabled()));
    }

}
