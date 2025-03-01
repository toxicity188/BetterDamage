package kr.toxicity.damage.manager

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.arguments.OfflinePlayerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import kr.toxicity.damage.api.ReloadState
import kr.toxicity.damage.api.data.DamageEffectData
import kr.toxicity.damage.api.manager.CommandManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.util.*
import org.bukkit.OfflinePlayer

object CommandManagerImpl : CommandManager {
    override fun reload(assets: PackAssets) {
    }

    override fun load() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(PLUGIN).silentLogs(true))
        CommandAPICommand("betterdamage")
            .withAliases("bd")
            .withFullDescription("BetterDamage's main command.")
            .withPermission("betterdamage")
            .withSubcommands(
                CommandAPICommand("reload")
                    .withAliases("re", "rl")
                    .withPermission("betterdamage.reload")
                    .executes(CommandExecutor { sender, _ ->
                        async {
                            when (val state = PLUGIN.reload()) {
                                is ReloadState.Failure -> state.throwable.handle(sender, "Unable to reload.")
                                is ReloadState.OnReload -> sender.sendMessage("This plugin is still on reload!")
                                is ReloadState.Success -> sender.sendMessage("Reload success: (${state.time.withComma()} ms)")
                            }
                        }
                    }),
                CommandAPICommand("test")
                    .withAliases("t")
                    .withPermission("betterdamage.test")
                    .withArguments(
                        StringArgument("effect")
                            .replaceSuggestions(ArgumentSuggestions.strings {
                                EffectManagerImpl.effectNames().toTypedArray()
                            }),
                        DoubleArgument("damage")
                    )
                    .executesPlayer(PlayerCommandExecutor exec@ { player, args ->
                        val e = args["effect"] as String
                        val d = args["damage"] as Double
                        val effect = EffectManagerImpl.effect(e) ?: return@exec player.sendMessage("This effect doesn't exist: $e")
                        effect.play(DamageEffectData(player, player, d))
                        player.sendMessage("Successfully played: $e")
                    }),
                CommandAPICommand("select")
                    .withAliases("s")
                    .withPermission("betterdamage.select")
                    .withArguments(
                        OfflinePlayerArgument("player"),
                        StringArgument("skin")
                            .replaceSuggestions(ArgumentSuggestions.strings {
                                DamageSkinManagerImpl.allNames().toTypedArray()
                            })
                    )
                    .executes(CommandExecutor { sender, args ->
                        val name = args["skin"] as String
                        val skin = DamageSkinManagerImpl.skin(name) ?: return@CommandExecutor sender.sendMessage("This skin doesn't exist: $name")
                        (args["player"] as OfflinePlayer).uniqueId.setPlayerData {
                            if (select(skin)) sender.sendMessage("Successfully selected: $name")
                            else sender.sendMessage("This player doesn't have this skin: $name")
                        }
                    }),
                CommandAPICommand("add")
                    .withAliases("a")
                    .withPermission("betterdamage.add")
                    .withArguments(
                        OfflinePlayerArgument("player"),
                        StringArgument("skin")
                            .replaceSuggestions(ArgumentSuggestions.strings {
                                DamageSkinManagerImpl.allNames().toTypedArray()
                            })
                    )
                    .executes(CommandExecutor { sender, args ->
                        val name = args["skin"] as String
                        val skin = DamageSkinManagerImpl.skin(name) ?: return@CommandExecutor sender.sendMessage("This skin doesn't exist: $name")
                        (args["player"] as OfflinePlayer).uniqueId.setPlayerData {
                            if (add(skin)) sender.sendMessage("Successfully added: $name")
                            else sender.sendMessage("This player already have this skin: $name")
                        }
                    }),
                CommandAPICommand("remove")
                    .withAliases("r")
                    .withPermission("betterdamage.remove")
                    .withArguments(
                        OfflinePlayerArgument("player"),
                        StringArgument("skin")
                            .replaceSuggestions(ArgumentSuggestions.strings {
                                DamageSkinManagerImpl.allNames().toTypedArray()
                            })
                    )
                    .executes(CommandExecutor { sender, args ->
                        val name = args["skin"] as String
                        val skin = DamageSkinManagerImpl.skin(name) ?: return@CommandExecutor sender.sendMessage("This skin doesn't exist: $name")
                        (args["player"] as OfflinePlayer).uniqueId.setPlayerData {
                            if (remove(skin)) sender.sendMessage("Successfully removed: $name")
                            else sender.sendMessage("This player doesn't have this skin: $name")
                        }
                    }),
                CommandAPICommand("info")
                    .withAliases("i")
                    .withPermission("betterdamage.info")
                    .withArguments(
                        OfflinePlayerArgument("player")
                    )
                    .executes(CommandExecutor { sender, args ->
                        (args["player"] as OfflinePlayer).getPlayerData {
                            sender.sendMessage("--------------------")
                            sender.sendMessage("Selected skin: ${selectedSkin()?.name()}")
                            sender.sendMessage("Skins:")
                            skins().forEach {
                                sender.sendMessage(" - ${it.name()}")
                            }
                            sender.sendMessage("--------------------")
                        }
                    }),
            )
            .executes(CommandExecutor { sender, _ ->
                sender.sendMessage("/betterdamage reload - Reloads this command.")
                sender.sendMessage("/betterdamage test <effect> <damage> - Tests some effect.")
                sender.sendMessage("/betterdamage info <offline-player> - Infos some player's data.")
                sender.sendMessage("/betterdamage select <offline-player> <damage skin> - Selects player's damage skin.")
                sender.sendMessage("/betterdamage add <offline-player> <damage skin> - Adds player's damage skin.")
                sender.sendMessage("/betterdamage remove <offline-player> <damage skin> - Removes player's damage skin.")
            })
            .register(PLUGIN)
    }

    override fun start() {
        CommandAPI.onEnable()
    }

    override fun end() {
        CommandAPI.onDisable()
    }
}