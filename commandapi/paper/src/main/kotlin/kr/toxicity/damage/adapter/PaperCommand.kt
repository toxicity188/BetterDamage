package kr.toxicity.damage.adapter

import com.destroystokyo.paper.profile.PlayerProfile
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIPaperConfig
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.PlayerProfileArgument
import dev.jorel.commandapi.executors.CommandArguments
import kr.toxicity.damage.api.adapter.CommandAdapter
import net.byteflux.libby.BukkitLibraryManager
import net.byteflux.libby.Library
import net.byteflux.libby.relocation.Relocation
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer
import kotlin.collections.asSequence
import kotlin.sequences.forEach

class PaperCommand(plugin: JavaPlugin) : CommandAdapter {

    init {
        BukkitLibraryManager(plugin, ".libs").run {
            addRepository("https://maven-central.storage-download.googleapis.com/maven2")
            addMavenCentral()
            loadLibrary(
                Library.builder()
                    .groupId("dev{}jorel")
                    .artifactId("commandapi-paper-shade")
                    .version("11.1.0")
                    .relocate(Relocation("dev{}jorel{}commandapi", "kr{}toxicity{}damage{}shaded{}dev{}jorel{}commandapi"))
                    .build()
            )
        }
        CommandAPI.onLoad(CommandAPIPaperConfig(plugin).fallbackToLatestNMS(true).silentLogs(true))
    }

    private val playerArgs = PlayerProfileArgument("player")

    override fun playerArgument(): Argument<*> = playerArgs

    @Suppress("DEPRECATION")
    override fun forEachOfflinePlayer(
        arguments: CommandArguments,
        playerConsumer: Consumer<OfflinePlayer>
    ) {
        arguments.getByArgument(playerArgs)
            ?.asSequence()
            ?.filterIsInstance<PlayerProfile>()
            ?.mapNotNull { it.uniqueId }
            ?.map { Bukkit.getOfflinePlayer(it) }
            ?.forEach(playerConsumer::accept)
    }
}