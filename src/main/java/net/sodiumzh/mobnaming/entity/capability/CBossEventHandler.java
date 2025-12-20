package net.sodiumzh.mobnaming.entity.capability;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.BossEvent;
import net.sodiumzh.mobnaming.network.MNUClientPacketHandlers;
import net.sodiumzh.mobnaming.network.MNUNetworkChannel;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteTag;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.sodiumzh.mobnaming.registry.MNUCaps;
import net.sodiumzh.mobnaming.util.Utils;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nonnull;

public interface CBossEventHandler extends INBTSerializable<ByteTag>
{
	public Mob getMob();
	public boolean isBossDisabled();
	public void setBossDisabled(boolean shouldDisable);
	public Optional<ServerBossEvent> getLinkedBossEvent();
	public void setLinkedBossEvent(@Nullable ServerBossEvent info);
	public void updateLinkedBossEvent();
	public void syncToClient();

	public static Optional<ServerBossEvent> getLinkedBossEvent(Mob mob)
	{
		//return Utils.getEntityCapabilityValue(mob, MNUCaps.BOSS_EVENT_HANDLER, CBossEventHandler::getLinkedBossEvent);
		return mob.getCapability(MNUCaps.BOSS_EVENT_HANDLER).resolve().flatMap(CBossEventHandler::getLinkedBossEvent);
	}
	
	public static boolean isBossDisabled(Mob mob)
	{
		return mob.getCapability(MNUCaps.BOSS_EVENT_HANDLER).filter(CBossEventHandler::isBossDisabled).isPresent();
	}
	
	public static class Impl implements CBossEventHandler
	{
		private Mob mob;
		private boolean isDisabled = false;
		private ServerBossEvent referredBossInfo = null;

		public Impl(Mob mob)
		{
			this.mob = mob;
		}
		
		@Override
		public Mob getMob() {
			return mob;
		}

		@Override
		public boolean isBossDisabled() {
			return isDisabled;
		}
		
		@Override
		public void setBossDisabled(boolean shouldDisable) {
			this.isDisabled = shouldDisable;
			this.updateLinkedBossEvent();
			if (!this.getMob().level().isClientSide)
				this.syncToClient();
		}

		@Override
		public ByteTag serializeNBT() {
			return ByteTag.valueOf(isDisabled);
		}

		@Override
		public void deserializeNBT(ByteTag nbt) {
			this.setBossDisabled(nbt.getAsByte() != 0);
		}

		@Override
		public Optional<ServerBossEvent> getLinkedBossEvent() {
			return Optional.ofNullable(referredBossInfo);
		}

		@Override
		public void setLinkedBossEvent(ServerBossEvent info) {
			referredBossInfo = info;
			this.updateLinkedBossEvent();
		}
		
		@Override
		public void updateLinkedBossEvent()
		{
			if (isDisabled)
				getLinkedBossEvent().ifPresent(bossEvent -> bossEvent.setVisible(false));
			else if (this.mob.isAlive())
				getLinkedBossEvent().ifPresent(bossEvent -> bossEvent.setVisible(true));
		}

		public void syncToClient()
		{
			Utils.sendToAllPlayers(this.mob.level(), MNUNetworkChannel.CHANNEL, new ClientboundSyncPacket(this.getMob().getId(), this.isBossDisabled()));
		}
	}
	
	public static class Prvd implements ICapabilitySerializable<ByteTag>
	{
		private Impl instance;
		
		public Prvd(Mob mob)
		{
			instance = new Impl(mob);
		}
		
		@Override
		public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
			if (cap == MNUCaps.BOSS_EVENT_HANDLER && instance != null)
				return LazyOptional.of(() -> this.instance).cast();
			else return LazyOptional.empty();
		}

		@Override
		public ByteTag serializeNBT() {
			return instance.serializeNBT();
		}

		@Override
		public void deserializeNBT(ByteTag nbt) {
			instance.deserializeNBT(nbt);
		}
	}

	public static class ClientboundSyncPacket implements Packet<ClientGamePacketListener>
	{

		private final int entityId;

		private final boolean isBossDisabled;
		public ClientboundSyncPacket(int entityId, boolean isBossDisabled)
		{
			this.entityId = entityId;
			this.isBossDisabled = isBossDisabled;
		}

		public ClientboundSyncPacket(FriendlyByteBuf buf)
		{
			this.entityId = buf.readInt();
			this.isBossDisabled = buf.readBoolean();
		}

		@Override
		public void write(FriendlyByteBuf buf)
		{
			buf.writeInt(entityId);
			buf.writeBoolean(isBossDisabled);
		}

		@Override
		public void handle(ClientGamePacketListener handler)
		{
			MNUClientPacketHandlers.handleBossEventHandlerSync(this, handler);
		}

		public int getEntityId() {
			return entityId;
		}

		public boolean isBossDisabled() {
			return isBossDisabled;
		}

	}



}
