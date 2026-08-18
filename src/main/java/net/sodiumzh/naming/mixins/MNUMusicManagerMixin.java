package net.sodiumzh.naming.mixins;

import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.naming.entity.BossEventStatics;
import net.sodiumzh.naming.registry.MNUEntityComponents;
import net.sodiumzh.naming.util.Utils;
import net.sodiumzh.nfu.mixin.NFUMixin;
import net.sodiumzh.nfu.util.NFUEntityStatics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MNUMusicManagerMixin implements NFUMixin<MusicManager> {

    @Inject(method = "startPlaying(Lnet/minecraft/sounds/Music;)V", at = @At("HEAD"), cancellable = true, remap = true)
    private void checkMusicSource(Music music, CallbackInfo callback)
    {
        Mob tickingMob = (Mob) NFUEntityStatics.getEntityTickStack().peekFirst();
        if (tickingMob == null) return;
        Class<? extends Mob> callerClass = Utils.getPossibleCallerMobClass(Mob.class);
        if (callerClass == null) return;
        if (callerClass.isAssignableFrom(tickingMob.getClass()) && BossEventStatics.isBossDisabled(tickingMob))
            callback.cancel();
    }

}
