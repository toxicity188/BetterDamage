package kr.toxicity.damage.api.player;

import kr.toxicity.damage.api.BetterDamage;
import kr.toxicity.damage.api.effect.DamageEffect;
import kr.toxicity.damage.api.skin.DamageSkin;
import kr.toxicity.damage.api.trigger.DamageTriggerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.UUID;

public interface DamagePlayerData {
    @Nullable DamageSkin selectedSkin();
    @NotNull @Unmodifiable
    Collection<DamageSkin> skins();

    boolean add(@NotNull DamageSkin skin);
    boolean remove(@NotNull DamageSkin skin);

    boolean select(@NotNull DamageSkin skin);

    default void save(@NotNull UUID uuid) throws Exception {
        BetterDamage.inst().databaseManager().connection().save(uuid, this);
    }

    default @Nullable DamageEffect effect(@NotNull DamageTriggerType type) {
        var inst = BetterDamage.inst();
        var selected = selectedSkin();
        if (selected == null) selected = inst.configManager().defaultSkin();
        if (selected == null) return inst.configManager().defaultEffect();
        else return selected.find(type);
    }
}
