package kr.toxicity.damage.api.adapter;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface CommandAdapter {
    @NotNull Argument<?> playerArgument();
    void forEachOfflinePlayer(@NotNull CommandArguments arguments, @NotNull Consumer<OfflinePlayer> playerConsumer);
}
