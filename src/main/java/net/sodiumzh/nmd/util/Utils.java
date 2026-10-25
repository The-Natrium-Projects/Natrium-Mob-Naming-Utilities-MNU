package net.sodiumzh.nmd.util;

import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.mutable.MutableObject;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;

public class Utils
{
	public static <T> Object reflectionGet(T obj, Class<? super T> declaredClass, String fieldNameSrg, boolean noStackTrace)
	{
		try
		{
			return ObfuscationReflectionHelper.getPrivateValue(declaredClass, obj, fieldNameSrg);
		}
		catch(Exception e)
		{
			if (!noStackTrace)
				e.printStackTrace();
			return null;
		}
	}
	
	public static <T> void reflectionSet(T obj, Class<? super T> declaredClass, String fieldNameSrg, Object value, boolean noStackTrace)
	{
		try
		{
			ObfuscationReflectionHelper.setPrivateValue(declaredClass, obj, value, fieldNameSrg);
		}
		catch(Exception e)
		{
			if (!noStackTrace)
				e.printStackTrace();
		}
	}
	
	public static <T, C> T getEntityCapabilityValue(Entity entity, Capability<C> cap, Function<C, T> access)
	{
		MutableObject<T> val = new MutableObject<>(null);
		entity.getCapability(cap).ifPresent(c -> val.setValue(access.apply(c)));
		return val.getValue();
	}

	public static SimpleChannel newChannel(String modId, String key, String version)
	{
		return NetworkRegistry.newSimpleChannel(
				new ResourceLocation(modId, key),
				() -> {
					return version;
				},
				(v) -> {
					return v.equals(version);
				},
				(v) -> {
					return v.equals(version);
				});
	}

	public static SimpleChannel newChannel(String modId, String key)
	{
		return newChannel(modId, key, "1.0");
	}

	@SuppressWarnings("resource")
	public static <T extends Packet<ClientGamePacketListener>>
	void registerDefaultClientGamePacket(int id, SimpleChannel channel, Class<T> packetClass)
	{
		channel.registerMessage(
				id,
				packetClass,
				(pack, buffer) -> {
					pack.write(buffer);
				},
				(buffer) -> {
					try
					{
						return packetClass.getConstructor(FriendlyByteBuf.class).newInstance(buffer);
					}
					catch (Exception e)
					{
						throw new IllegalArgumentException("NaUtilsNetworkStatics::registerDefaultPacket packet class missing constructor.", e);
					}
				},
				(pack, ctx) -> {
					ctx.get().enqueueWork(() ->
					{
						pack.handle(Minecraft.getInstance().getConnection());
					});
					ctx.get().setPacketHandled(true);
				}
		);
	}

	public static void sendToAllPlayers(Level level, SimpleChannel channel, Packet<?> message)
	{
		if (level.isClientSide)
			return;
		for (Player player: level.players())
		{
			if (player instanceof ServerPlayer sp)
			{
				channel.send(PacketDistributor.PLAYER.with(() -> sp), message);
			}
		}
	}

	@Nullable
	public static <T> Class<? extends T> getPossibleCallerMobClass(Class<T> parent)
	{
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stacktrace.length; ++i)
		{
			String clsName = stacktrace[i].getClassName();

			try {
				Class<?> cls = Class.forName(clsName);
				if (parent.isAssignableFrom(cls))
					return (Class<? extends T>) cls;
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
}
