package kr.toxicity.damage.api.event;

import kr.toxicity.damage.api.data.DamageEffectData;
import kr.toxicity.damage.api.effect.DamageEffect;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when damage effect is created.
 */
@Getter
@Setter
public class CreateDamageEffectEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final DamageEffect effect;
    private final DamageEffectData data;
    private boolean cancelled;

    /**
     * Creates damage event
     * @param effect effect
     * @param data effect data
     */
    public CreateDamageEffectEvent(@NotNull DamageEffect effect, @NotNull DamageEffectData data) {
        super(data.player());
        this.effect = effect;
        this.data = data;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Gets a handler list
     * @return handler list
     */
    @SuppressWarnings("used")
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
