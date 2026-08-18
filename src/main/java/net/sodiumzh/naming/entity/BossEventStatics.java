package net.sodiumzh.naming.entity;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.Mob;
import net.sodiumzh.nfu.entity.component.EntityComponentAPI;

import java.util.Optional;

public class BossEventStatics {

    public static final String KEY_BOSS_DISABLED = "natriumnaming_isBossDisabled";
    public static final String KEY_LINKED_BOSS_EVENT = "natriumnaming_linkedBossEvent";

    public static boolean isBossDisabled(Mob m) {
        return EntityComponentAPI.getDefaultSyncher(m).getSynchedData(KEY_BOSS_DISABLED, Boolean.class).orElse(false);
    }

    public static void setBossDisabled(Mob m, boolean shouldDisable) {
        EntityComponentAPI.getDefaultSyncher(m).setSynchedData(KEY_BOSS_DISABLED, Boolean.class, shouldDisable);
    }

    public static Optional<ServerBossEvent> getLinkedBossEvent(Mob m) {
        return EntityComponentAPI.getDataComponent(m).getVariable(KEY_LINKED_BOSS_EVENT, ServerBossEvent.class);
    }

    public static void setLinkedBossEvent(Mob m, ServerBossEvent info) {
        EntityComponentAPI.getDataComponent(m).putTransientVariable(KEY_LINKED_BOSS_EVENT, info);
    }

    public static void updateLinkedBossEvent(Mob m)
    {
        boolean isDisabled = isBossDisabled(m);
        if (isDisabled)
            getLinkedBossEvent(m).ifPresent(bossEvent -> bossEvent.setVisible(false));
        else if (m.isAlive())
            getLinkedBossEvent(m).ifPresent(bossEvent -> bossEvent.setVisible(true));
    }

}
