package net.sodiumzh.nmd.mixins;

import java.util.Optional;

import net.sodiumzh.nmd.entity.BossEventStatics;
import net.sodiumzh.nmd.entity.IBossEventMobAccessor;
import net.sodiumzh.nfu.mixin.NFUMixin;
import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;

@Mixin(BossEvent.class)
public class NMDBossEventMixin implements NFUMixin<BossEvent>
{

	@Unique
    private boolean isBlocked()
	{
		if (caller() instanceof ServerBossEvent bossEvent && caller() instanceof IBossEventMobAccessor accessor)
		{
			return Optional.ofNullable(accessor.getLinkedMob())
				.filter(BossEventStatics::isBossDisabled).isPresent();
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
