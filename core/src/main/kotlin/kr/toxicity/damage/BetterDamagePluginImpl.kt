package kr.toxicity.damage

import kr.toxicity.damage.api.BetterDamage
import kr.toxicity.damage.api.BetterDamagePlugin
import kr.toxicity.damage.api.ReloadState
import kr.toxicity.damage.api.adapter.ModelAdapter
import kr.toxicity.damage.api.manager.CommandManager
import kr.toxicity.damage.api.manager.CompatibilityManager
import kr.toxicity.damage.api.manager.ConfigManager
import kr.toxicity.damage.api.manager.DamageManager
import kr.toxicity.damage.api.manager.DamageSkinManager
import kr.toxicity.damage.api.manager.DatabaseManager
import kr.toxicity.damage.api.manager.EffectManager
import kr.toxicity.damage.api.manager.ImageManager
import kr.toxicity.damage.api.manager.PackManager
import kr.toxicity.damage.api.manager.PlayerManager
import kr.toxicity.damage.api.manager.TriggerManager
import kr.toxicity.damage.api.nms.NMS
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.api.pack.PackPath
import kr.toxicity.damage.api.pack.PackSupplier
import kr.toxicity.damage.api.scheduler.DamageScheduler
import kr.toxicity.damage.api.util.MinecraftVersion
import kr.toxicity.damage.compatibility.modelengine.CurrentModelEngineAdapter
import kr.toxicity.damage.compatibility.modelengine.LegacyModelEngineAdapter
import kr.toxicity.damage.manager.CommandManagerImpl
import kr.toxicity.damage.manager.CompatibilityManagerImpl
import kr.toxicity.damage.manager.ConfigManagerImpl
import kr.toxicity.damage.manager.DamageSkinManagerImpl
import kr.toxicity.damage.manager.DatabaseManagerImpl
import kr.toxicity.damage.manager.EffectManagerImpl
import kr.toxicity.damage.manager.ImageManagerImpl
import kr.toxicity.damage.manager.PackManagerImpl
import kr.toxicity.damage.manager.PlayerManagerImpl
import kr.toxicity.damage.manager.TriggerManagerImpl
import kr.toxicity.damage.scheduler.BukkitScheduler
import kr.toxicity.damage.scheduler.FoliaScheduler
import kr.toxicity.damage.util.DATA_FOLDER
import kr.toxicity.damage.util.async
import kr.toxicity.damage.util.handle
import kr.toxicity.damage.util.info
import kr.toxicity.damage.util.warn
import kr.toxicity.damage.version.ModelEngineVersion
import kr.toxicity.model.api.tracker.EntityTracker
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStream
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.BiConsumer
import java.util.jar.JarFile

class BetterDamagePluginImpl : JavaPlugin(), BetterDamagePlugin {

    private val version = MinecraftVersion(Bukkit.getBukkitVersion().substringBefore('-'))
    private val scheduler = if (BetterDamage.IS_PAPER) FoliaScheduler() else BukkitScheduler()
    private val onReload = AtomicBoolean()

    private lateinit var nms: NMS
    private var modelAdapter: ModelAdapter? = null

    private val managers by lazy {
        listOf(
            ConfigManagerImpl,
            CommandManagerImpl,
            CompatibilityManagerImpl,
            TriggerManagerImpl,

            ImageManagerImpl,
            EffectManagerImpl,
            DamageSkinManagerImpl,

            PlayerManagerImpl,
            PackManagerImpl,
            DatabaseManagerImpl
        )
    }

    override fun onLoad() {
        BetterDamage.inst(this)
        managers.forEach(DamageManager::load)
    }

    override fun onEnable() {
        val manager = Bukkit.getPluginManager()
        nms = when (version) {
            MinecraftVersion.V1_21_4 -> kr.toxicity.damage.nms.v1_21_R3.NMSImpl()
            MinecraftVersion.V1_21_2, MinecraftVersion.V1_21_3 -> kr.toxicity.damage.nms.v1_21_R2.NMSImpl()
            MinecraftVersion.V1_21, MinecraftVersion.V1_21_1 -> kr.toxicity.damage.nms.v1_21_R1.NMSImpl()
            MinecraftVersion.V1_20_5, MinecraftVersion.V1_20_6 -> kr.toxicity.damage.nms.v1_20_R4.NMSImpl()
            MinecraftVersion.V1_20_3, MinecraftVersion.V1_20_4 -> kr.toxicity.damage.nms.v1_20_R3.NMSImpl()
            MinecraftVersion.V1_20_2 -> kr.toxicity.damage.nms.v1_20_R2.NMSImpl()
            MinecraftVersion.V1_20, MinecraftVersion.V1_20_1 -> kr.toxicity.damage.nms.v1_20_R1.NMSImpl()
            MinecraftVersion.V1_19_4 -> kr.toxicity.damage.nms.v1_19_R3.NMSImpl()
            else -> {
                warn(
                    "Unsupported version: $version",
                    "Plugin will be automatically disabled."
                )
                manager.disablePlugin(this)
                return
            }
        }
        manager.getPlugin("ModelEngine")?.let {
            runCatching {
                @Suppress("DEPRECATION")
                val version = ModelEngineVersion(it.description.version)
                modelAdapter = if (version >= ModelEngineVersion.version_4_0_0) CurrentModelEngineAdapter() else LegacyModelEngineAdapter()
                info("ModelEngine support enabled: $version")
            }.onFailure { e ->
                e.handle("Failed to load ModelEngine support.")
            }
        } ?: run {
            if (manager.isPluginEnabled("BetterModel")) modelAdapter = ModelAdapter {
                EntityTracker.tracker(it.uniqueId)?.height()
            }
        }
        info("Plugin enabled.")
        managers.forEach(DamageManager::start)
        async {
            when (val state = reload()) {
                is ReloadState.Failure -> state.throwable.handle("Unable to reload.")
                is ReloadState.OnReload -> warn("Plugin load failed.")
                is ReloadState.Success -> info("Plugin has successfully loaded.")
            }
        }
    }

    override fun onDisable() {
        managers.forEach(DamageManager::end)
        info("Plugin disabled.")
    }

    override fun handle(throwable: Throwable, message: String) {
        throwable.handle(message)
    }

    override fun version(): MinecraftVersion = version
    override fun scheduler(): DamageScheduler = scheduler
    override fun modelAdapter(): ModelAdapter? = modelAdapter

    override fun nms(): NMS = nms

    override fun reload(): ReloadState {
        if (onReload()) return ReloadState.ON_RELOAD
        return runCatching {
            val time = System.currentTimeMillis()
            val assets = PackAssets()
            if (!DATA_FOLDER.exists()) loadAssets("default") { path, stream ->
                File(DATA_FOLDER, path).apply {
                    parentFile.mkdirs()
                }.outputStream().buffered().use { output ->
                    stream.copyTo(output)
                }
            }
            managers.forEach {
                it.reload(assets)
            }
            ReloadState.Success(System.currentTimeMillis() - time, assets.build().apply {
                PackManagerImpl.pack(this)
            })
        }.getOrElse {
            ReloadState.Failure(it)
        }
    }

    override fun onReload(): Boolean = onReload.get()

    override fun configManager(): ConfigManager = ConfigManagerImpl
    override fun commandManager(): CommandManager = CommandManagerImpl
    override fun imageManager(): ImageManager = ImageManagerImpl
    override fun packManager(): PackManager = PackManagerImpl
    override fun effectManager(): EffectManager = EffectManagerImpl
    override fun playerManager(): PlayerManager = PlayerManagerImpl
    override fun triggerManager(): TriggerManager = TriggerManagerImpl
    override fun damageSkinManager(): DamageSkinManager = DamageSkinManagerImpl
    override fun databaseManager(): DatabaseManager = DatabaseManagerImpl
    override fun compatibilityManager(): CompatibilityManager = CompatibilityManagerImpl

    override fun loadAssets(prefix: String, consumer: BiConsumer<String, InputStream>) {
        loadAssets(prefix) { s, i ->
            consumer.accept(s, i)
        }
    }

    private fun loadAssets(prefix: String, consumer: (String, InputStream) -> Unit) {
        JarFile(file).use {
            it.entries().toList().parallelStream().forEach { entry ->
                if (!entry.name.startsWith(prefix)) return@forEach
                if (entry.name.length <= prefix.length + 1) return@forEach
                val name = entry.name.substring(prefix.length + 1)
                if (!entry.isDirectory) it.getInputStream(entry).buffered().use { stream ->
                    consumer(name, stream)
                }
            }
        }
    }
}