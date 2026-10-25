package net.sodiumzh.nmd.entity;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.nmd.mixins.NMDServerBossEventMixin;

import javax.annotation.Nullable;

/**
 * Used to access the extra mob field of {@link ServerBossEvent}. It will be added to ServerBossEvent class and you can
 * cast ServerBossEvent to this interface.
 * <p>Implemented by mixin {@link NMDServerBossEventMixin}.
 */
public interface IBossEventMobAccessor {

    @Nullable
    public Mob getLinkedMob();

    public void setLinkedMob(@Nullable Mob mob);
}
