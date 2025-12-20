package net.sodiumzh.mobnaming.mixins;

import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.mobnaming.entity.capability.CBossEventHandler;
import net.sodiumzh.mobnaming.registry.MNUCaps;
import net.sodiumzh.mobnaming.util.Utils;
import net.sodiumzh.nfu.mixin.NFUMixin;
import net.sodiumzh.nfu.util.NFUEntityStatics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MNUMusicManagerMixin implements NFUMixin<MusicManager> {

    private MusicManager getThis()
    {
        return (MusicManager) (Object) this;
    }

    @Inject(method = "startPlaying(Lnet/minecraft/sounds/Music;)V", at = @At("HEAD"), cancellable = true)
    private void checkMusicSource(Music music, CallbackInfo callback)
    {
        Mob tickingMob = (Mob) NFUEntityStatics.getEntityTickStack().getTop()
            .filter(e -> e instanceof Mob).orElse(null);
        if (tickingMob == null) return;
        Class<? extends Mob> callerClass = Utils.getPossibleCallerMobClass(Mob.class);
        if (callerClass == null) return;
        if (callerClass.isAssignableFrom(tickingMob.getClass()) && Utils.getEntityCapabilityValue(tickingMob,
                MNUCaps.BOSS_EVENT_HANDLER, CBossEventHandler::isBossDisabled))
            callback.cancel();
    }


}
