package io.github.linsminecraftstudio.mxlib.chat.input;

import org.bukkit.entity.Player;

import java.util.function.Predicate;

public interface ChatInputHandler extends Predicate<String> {
    void handleInput(Player player, String input);
}