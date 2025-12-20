package net.sodiumzh.mobnaming.entity.capability;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.mobnaming.mixins.MNUServerBossEventMixin;

import javax.annotation.Nullable;

/**
 * Used to access the extra mob field of {@link ServerBossEvent}. Implemented by mixin
 * {@link MNUServerBossEventMixin}.
 */
public interface IBossEventMobAccessor {

    @Nullable
    public Mob getLinkedMob();

    public void setLinkedMob(@Nullable Mob mob);
}
