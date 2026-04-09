package kr.toxicity.damage.command

import org.bukkit.command.CommandSender
import org.incendo.cloud.CommandManager
import org.incendo.cloud.description.Description

class CommandBuildContext(
    val manager: CommandManager<CommandSender>,
    name: String,
    description: String,
    vararg aliases: String,
) {
    val root = CommandBuilder(
        null,
        this,
        CommandBuilder.Info(name, Description.description(description), aliases.toList())
    )

    fun build() {
        root.build().forEach {
            manager.command(it)
        }
    }
}
