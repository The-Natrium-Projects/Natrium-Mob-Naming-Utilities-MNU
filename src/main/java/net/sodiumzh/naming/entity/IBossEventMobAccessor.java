package net.sodiumzh.naming.entity;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.naming.mixins.MNUServerBossEventMixin;

import javax.annotation.Nullable;

/**
 * Used to access the extra mob field of {@link ServerBossEvent}. It will be added to ServerBossEvent class and you can
 * cast ServerBossEvent to this interface.
 * <p>Implemented by mixin {@link MNUServerBossEventMixin}.
 */
public interface IBossEventMobAccessor {

    @Nullable
    public Mob getLinkedMob();

    public void setLinkedMob(@Nullable Mob mob);
}
