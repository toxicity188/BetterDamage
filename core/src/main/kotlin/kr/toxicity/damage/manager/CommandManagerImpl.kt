package kr.toxicity.damage.manager

import kr.toxicity.damage.api.ReloadState
import kr.toxicity.damage.api.data.DamageEffectData
import kr.toxicity.damage.api.manager.CommandManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.command.effect
import kr.toxicity.damage.command.register
import kr.toxicity.damage.command.skin
import kr.toxicity.damage.util.*
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser.offlinePlayerParser
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager
import org.incendo.cloud.parser.standard.DoubleParser.doubleParser
import org.incendo.cloud.parser.standard.StringParser.stringParser
import org.incendo.cloud.suggestion.SuggestionProvider.blockingStrings

object CommandManagerImpl : CommandManager {
    override fun reload(assets: PackAssets) {
    }

    override fun start() {
        LegacyPaperCommandManager.createNative(
            PLUGIN,
            ExecutionCoordinator.simpleCoordinator()
        ).apply {
            if (hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
                registerBrigadier()
                brigadierManager().setNativeNumberSuggestions(true)
            } else if (hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) registerAsynchronousCompletions()
        }.register(
            "betterdamage",
            "All-related command.",
            "bd"
        ) {
            create(
                "reload",
                "Reloads BetterDamage.",
                "re", "rl"
            ) {
                handler(::reload)
            }
            create(
                "test",
                "Tests effects.",
                "t"
            ) {
                senderType(Player::class.java)
                    .required("effect", stringParser(), blockingStrings { _, _ -> EffectManagerImpl.effectNames() })
                    .required("damage", doubleParser())
                    .handler(::test)
            }
            create(
                "select",
                "Selects player's damage skin.",
                "s"
            ) {
                required("player", offlinePlayerParser())
                    .required("skin", stringParser(), blockingStrings { _, _ -> DamageSkinManagerImpl.allNames() })
                    .handler(::select)
            }
            create(
                "add",
                "Adds player's damage skin.",
                "a"
            ) {
                required("player", offlinePlayerParser())
                    .required("skin", stringParser(), blockingStrings { _, _ -> DamageSkinManagerImpl.allNames() })
                    .handler(::add)
            }
            create(
                "remove",
                "Removes player's damage skin.",
                "r"
            ) {
                required("player", offlinePlayerParser())
                    .required("skin", stringParser(), blockingStrings { _, _ -> DamageSkinManagerImpl.allNames() })
                    .handler(::remove)
            }
            create(
                "info",
                "Infos some player's data.",
                "i"
            ) {
                required("player", offlinePlayerParser())
                    .handler(::info)
            }
        }
    }

    private fun reload(ctx: CommandContext<CommandSender>) {
        val sender = ctx.sender()
        async {
            when (val state = PLUGIN.reload()) {
                is ReloadState.Failure -> state.throwable.handle(sender, "Unable to reload.")
                is ReloadState.OnReload -> sender.audience().warn("This plugin is still on reload!")
                is ReloadState.Success -> sender.audience().info("Reload success: (${state.time.withComma()} ms)")
            }
        }
    }

    private fun test(ctx: CommandContext<Player>) {
        val player = ctx.sender()
        val effect = ctx.effect("effect") { return player.audience().warn("This effect doesn't exist: $it") }
        val d = ctx.get<Double>("damage")
        effect.play(DamageEffectData(player, player, d))
        player.audience().info("Successfully played.")
    }

    private fun select(ctx: CommandContext<CommandSender>) {
        val offline = ctx.get<OfflinePlayer>("player")
        val sender = ctx.sender().audience()
        val skin = ctx.skin("skin") { return sender.warn("This skin doesn't exist: $it") }
        offline.setPlayerData {
            if (select(skin)) sender.info("Successfully selected: ${skin.name()}")
            else sender.warn("This player doesn't have this skin: ${skin.name()}")
        }
    }

    private fun add(ctx: CommandContext<CommandSender>) {
        val offline = ctx.get<OfflinePlayer>("player")
        val sender = ctx.sender().audience()
        val skin = ctx.skin("skin") { return sender.warn("This skin doesn't exist: $it") }
        offline.setPlayerData {
            if (add(skin)) sender.info("Successfully added: ${skin.name()}")
            else sender.warn("This player already have this skin: ${skin.name()}")
        }
    }

    private fun remove(ctx: CommandContext<CommandSender>) {
        val offline = ctx.get<OfflinePlayer>("player")
        val sender = ctx.sender().audience()
        val skin = ctx.skin("skin") { return sender.warn("This skin doesn't exist: $it") }
        offline.setPlayerData {
            if (remove(skin)) sender.info("Successfully removed: ${skin.name()}")
            else sender.warn("This player doesn't have this skin: ${skin.name()}")
        }
    }

    private fun info(ctx: CommandContext<CommandSender>) {
        val offline = ctx.get<OfflinePlayer>("player")
        val sender = ctx.sender().audience()
        offline.getPlayerData {
            sender.info("--------------------")
            sender.info("Selected skin: ${selectedSkin()?.name()}")
            sender.info("Skins:")
            skins().forEach {
                sender.info(" - ${it.name()}")
            }
            sender.info("--------------------")
        }
    }
}