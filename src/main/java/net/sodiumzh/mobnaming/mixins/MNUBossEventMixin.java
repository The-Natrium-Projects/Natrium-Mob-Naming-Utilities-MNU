package net.sodiumzh.mobnaming.mixins;

import java.util.Optional;

import net.sodiumzh.mobnaming.entity.capability.IBossEventMobAccessor;
import net.sodiumzh.nfu.mixin.NFUMixin;
import net.sodiumzh.mobnaming.entity.capability.CBossEventHandler;
import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.mobnaming.BossInfoReferences;
import net.sodiumzh.mobnaming.registry.MNUCaps;
import net.sodiumzh.mobnaming.util.Utils;

@Mixin(BossEvent.class)
public class MNUBossEventMixin implements NFUMixin<BossEvent>
{

	private boolean isBlocked()
	{
		if (caller() instanceof ServerBossEvent bossEvent && caller() instanceof IBossEventMobAccessor accessor)
		{
			return Optional.ofNullable(accessor.getLinkedMob())
				.flatMap(mob -> mob.getCapability(MNUCaps.BOSS_EVENT_HANDLER).resolve())
				.filter(CBossEventHandler::isBossDisabled).isPresent();
		}
		else return false;
	}
	
	@ModifyReturnValue(method = "shouldDarkenScreen()Z", at = @At("RETURN"))
	private boolean checkDarkenScreen(boolean original)
	{
		return original && !isBlocked();
	}
	
	@ModifyReturnValue(method = "shouldPlayBossMusic()Z", at = @At("RETURN"))
	private boolean checkPlayMusic(boolean original)
	{
		return original && !isBlocked();
	}
	
	@ModifyReturnValue(method = "shouldCreateWorldFog()Z", at = @At("RETURN"))
	private boolean checkCreateFog(boolean original)
	{
		return original && !isBlocked();
	}
}
