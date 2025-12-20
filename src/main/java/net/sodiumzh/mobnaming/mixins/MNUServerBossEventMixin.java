package net.sodiumzh.mobnaming.mixins;

import java.util.List;
import java.util.Optional;

import net.minecraft.network.chat.Component;
import net.sodiumzh.mobnaming.entity.capability.CBossEventHandler;
import net.sodiumzh.mobnaming.entity.capability.IBossEventMobAccessor;
import net.sodiumzh.nfu.container.IStackContainer;
import net.sodiumzh.nfu.mixin.NFUMixin;
import net.sodiumzh.nfu.util.NFUEntityStatics;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.mobnaming.BossInfoReferences;
import net.sodiumzh.mobnaming.registry.MNUCaps;
import net.sodiumzh.mobnaming.util.Utils;

import javax.annotation.Nullable;

@Mixin(ServerBossEvent.class)
public class MNUServerBossEventMixin implements NFUMixin<ServerBossEvent>, IBossEventMobAccessor
{

	private int backTracedCount = 0;

	// null = not referred; empty = referred not to be linked to specific mob.
	@Unique
	@Nullable
	private Optional<Mob> linkedMob = null;

	@Unique
	private boolean isBlocked()
	{
		return Optional.ofNullable(linkedMob).orElse(Optional.empty())
			.flatMap(mob -> mob.getCapability(MNUCaps.BOSS_EVENT_HANDLER).resolve())
			.filter(CBossEventHandler::isBossDisabled).isPresent();
	}

	@Inject(method = "setDarkenScreen(Z)Lnet/minecraft/world/BossEvent;",
			at = @At("HEAD"), cancellable = true)
	private void checkSetDarkScreen(boolean value, CallbackInfoReturnable<BossEvent> callback)
	{
		if (value && isBlocked())
			callback.setReturnValue(caller());
	}
	
	@Inject(method = "setPlayBossMusic(Z)Lnet/minecraft/world/BossEvent;",
			at = @At("HEAD"), cancellable = true)
	private void checkSetPlayMusic(boolean value, CallbackInfoReturnable<BossEvent> callback)
	{
		if (value && isBlocked())
			callback.setReturnValue(caller());
	}
	
	@Inject(method = "setCreateWorldFog(Z)Lnet/minecraft/world/BossEvent;",
			at = @At("HEAD"), cancellable = true)
	private void checkSetCreateFog(boolean value, CallbackInfoReturnable<BossEvent> callback)
	{
		if (value && isBlocked())
			callback.setReturnValue(caller());
	}
	
	@Inject(method = "setVisible(Z)V",
			at = @At("HEAD"), cancellable = true)
	private void checkSetVisible(boolean value, CallbackInfo callback)
	{
		if (value && isBlocked())
			callback.cancel();
	}
	
	@ModifyReturnValue(method = "isVisible()Z", at = @At("RETURN"))
	private boolean checkVisible(boolean original)
	{
		return original && !isBlocked();
	}

	@Override
	public @Nullable Mob getLinkedMob() {
		return linkedMob != null ? linkedMob.orElse(null) : null;
	}

	@Override
	public void setLinkedMob(@Nullable Mob mob) {
		this.linkedMob = Optional.ofNullable(mob);
	}

	/**
	 * Try referring updating mob from stack walker. It will catch
	 */
	@Unique
	private void tryReferMobUpdate() {
		if (linkedMob != null) return;
		Mob tickingMob = NFUEntityStatics.getEntityTickStack().backTraceList()
				.stream().filter(e -> e instanceof Mob).map(e -> (Mob)e).findFirst().orElse(null);
		if (tickingMob == null) return;
		StackWalker.getInstance().walk(frames -> {
			// Find the stack frame belonging to the ticking entity
			frames.filter(frame -> Mob.class.isAssignableFrom(frame.getDeclaringClass())
				&& frame.getDeclaringClass().isAssignableFrom(tickingMob.getClass()))
				.findFirst().ifPresent(sf -> {
					if (linkedMob.map(m -> m.getCapability(MNUCaps.BOSS_EVENT_HANDLER).orElse(null))
						.filter(c -> c.getLinkedBossEvent().filter(be -> be.equals(caller())).isEmpty())
						.isPresent())
					{
						linkedMob = Optional.of(tickingMob);
					}


				});
			return 0;
		});
	}


}
