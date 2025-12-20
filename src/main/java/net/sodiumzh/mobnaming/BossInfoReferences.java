package net.sodiumzh.mobnaming;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.sodiumzh.mobnaming.util.Utils;

/**
 * All storage for referred mapping of mob and boss info.
 * <p>Boss bar linkage is referred under the following ways on a mob joins level:
 * <p>(1) If the boss bar access method of this mob's type has been already recorded, directly access it;
 * <p>(2) Then scan the mob's declared fields and its parent class' fields (until {@code Mob} class), and if
 * a {@link BossEvent} or {@link ServerBossEvent} field is found, label it as linked;
 * <p>(3)
 */
public class BossInfoReferences
{

	private static final Map<EntityType<? extends Mob>, Optional<Function<Mob, ServerBossEvent>>> ACCESSORS
		= new HashMap<>();



	@SuppressWarnings("unchecked")
	public static <T extends Mob> void addKnownAccessMethod(EntityType<T> type, Function<T, ServerBossEvent> accessor) {
		ACCESSORS.put(type, Optional.of((Mob mob) -> accessor.apply((T)mob)));
	}

	private static Optional<Field> scanBossEventField(@Nonnull Class<? extends Mob> clazz) {
		AtomicReference<Field> res = new AtomicReference<>(null);
		AtomicReference<Class<?>> currentClass = new AtomicReference<>(clazz);
		while (res.get() == null) {
			Arrays.stream(clazz.getDeclaredFields())
				.filter(fld -> BossEvent.class.isAssignableFrom(fld.getType()))
				.findAny().ifPresent(res::set);
			currentClass.set(currentClass.get().getSuperclass());
			if (!Mob.class.isAssignableFrom(currentClass.get())) break;
		}
		if (res.get() != null) res.get().setAccessible(true);
		return Optional.ofNullable(res.get());
	}

	@SuppressWarnings("unchecked")
	public static void recordBossEventAccessor(Mob mob) {
		if (ACCESSORS.containsKey(mob.getType())) return;
		Optional<Field> optfld = scanBossEventField(mob.getClass());
		optfld.ifPresentOrElse(fld -> ACCESSORS.put((EntityType<? extends Mob>) mob.getType(), Optional.of((Mob m) -> {
			try {
				Object obj = fld.get(m);
				if (obj instanceof ServerBossEvent sbe)
					return sbe;
				else return null;
			} catch (Exception e) {
				return null;
			}
		})), () -> ACCESSORS.put((EntityType<? extends Mob>) mob.getType(), Optional.empty()));
	}

	public static Optional<ServerBossEvent> getLinkedBossEvent(Mob mob) {
		try {
			return Optional.ofNullable(ACCESSORS.get(mob.getType())).orElse(Optional.empty())
				.map(f -> f.apply(mob));
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
