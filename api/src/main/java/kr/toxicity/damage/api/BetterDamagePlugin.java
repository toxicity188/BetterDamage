package kr.toxicity.damage.api;

import com.vdurmont.semver4j.Semver;
import kr.toxicity.damage.api.adapter.ModelAdapter;
import kr.toxicity.damage.api.manager.*;
import kr.toxicity.damage.api.nms.NMS;
import kr.toxicity.damage.api.scheduler.DamageScheduler;
import kr.toxicity.damage.api.util.MinecraftVersion;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * A plugin instance of BetterDamage
 * @see org.bukkit.plugin.java.JavaPlugin
 * @since 1.0
 */
public interface BetterDamagePlugin {

    /**
     * Gets logger.
     * @return logger
     */
    @NotNull Logger getLogger();

    /**
     * Gets semver.
     * @return semver
     */
    @NotNull Semver semver();

    /**
     * Gets audiences
     * @return audiences
     */
    @NotNull BukkitAudiences audiences();

    /**
     * Handles exception
     * @param throwable exception
     * @param message log
     */
    void handle(@NotNull Throwable throwable, @NotNull String message);

    /**
     * Gets Minecraft version of server.
     * @return server version
     */
    @NotNull MinecraftVersion version();

    /**
     * Gets plugin's scheduler.
     * @return scheduler
     */
    @NotNull DamageScheduler scheduler();

    /**
     * Gets model adapter
     * @return model adapter
     */
    @Nullable ModelAdapter modelAdapter();

    /**
     * Gets NMS code.
     * @return NMS
     */
    @NotNull NMS nms();

    /**
     * Checks this plugin is still on reload.
     * @return whether on reload
     */
    boolean onReload();

    /**
     * Executes reload.
     * @return reload state
     */
    @NotNull ReloadState reload();

    /**
     * Gets config manager.
     * @return config manager
     */
    @NotNull ConfigManager configManager();

    /**
     * Gets command manager.
     * @return command manager
     */
    @NotNull CommandManager commandManager();

    /**
     * Gets image manager.
     * @return image manager
     */
    @NotNull ImageManager imageManager();

    /**
     * Gets pack manager.
     * @return pack manager
     */
    @NotNull PackManager packManager();

    /**
     * Gets effect manager.
     * @return effect manager
     */
    @NotNull EffectManager effectManager();

    /**
     * Gets player manager.
     * @return player manager
     */
    @NotNull PlayerManager playerManager();

    /**
     * Gets trigger manager.
     * @return trigger manager
     */
    @NotNull TriggerManager triggerManager();

    /**
     * Gets damage skin manager.
     * @return damage skin manager
     */
    @NotNull DamageSkinManager damageSkinManager();

    /**
     * Gets database manager.
     * @return database manager
     */
    @NotNull DatabaseManager databaseManager();

    /**
     * Gets compatibility manager.
     * @return compatibility manager
     */
    @NotNull CompatibilityManager compatibilityManager();

    /**
     * Loads assets
     */
    void loadAssets(@NotNull String prefix, @NotNull BiConsumer<String, InputStream> consumer);
}
