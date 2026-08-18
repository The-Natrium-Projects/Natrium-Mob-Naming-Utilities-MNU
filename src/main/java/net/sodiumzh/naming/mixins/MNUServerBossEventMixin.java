package net.sodiumzh.naming.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.naming.entity.BossEventStatics;
import net.sodiumzh.naming.entity.IBossEventMobAccessor;
import net.sodiumzh.nfu.mixin.NFUMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(ServerBossEvent.class)
public class MNUServerBossEventMixin implements NFUMixin<ServerBossEvent>, IBossEventMobAccessor
{

	private int backTracedCount = 0;

	// null = not referred; empty = referred not to be linked to specific mob.
	@Unique
	@Nullable
	private Mob linkedMob = null;

	@Unique
	private boolean isBlocked()
	{
		return Optional.ofNullable(linkedMob).filter(BossEventStatics::isBossDisabled).isPresent();
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
		return linkedMob;
	}

	@Override
	public void setLinkedMob(@Nullable Mob mob) {
		this.linkedMob = mob;
	}

}
