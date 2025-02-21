package io.github.linsminecraftstudio.mxlib.chat.input;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A class for handling chat input from a player.
 * <br>
 * Copied from <a href="https://github.com/Slimefun4/dough">dough</a>
 */
public final class ChatInput {

    static ChatInputListener listener;

    private ChatInput() {}

    /**
     * Get player input from callback.
     * @param plugin the plugin instance.
     * @param p the player.
     * @param handler the callback to handle the input.
     */
    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull Consumer<String> handler) {
        waitForPlayer(plugin, p, s -> true, handler);
    }

    /**
     * Get player input from callback with a predicate.
     * @param plugin the plugin instance.
     * @param p the player.
     * @param predicate the predicate to test the input.
     * @param handler the callback to handle the input.
     */
    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull Predicate<String> predicate, @Nonnull Consumer<String> handler) {
        queue(plugin, p, new ChatInputHandler() {

            @Override
            public boolean test(String msg) {
                return predicate.test(msg);
            }

            @Override
            public void handleInput(Player p, String msg) {
                handler.accept(msg);
            }

        });
    }

    /**
     * Get player input from callback with a predicate.
     * @param plugin the plugin instance.
     * @param p the player.
     * @param handler the callback to handle the input.
     */
    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull BiConsumer<Player, String> handler) {
        waitForPlayer(plugin, p, s -> true, handler);
    }

    /**
     * Get player input from callback with a predicate.
     * @param plugin the plugin instance.
     * @param p the player.
     * @param predicate the predicate to test the input.
     * @param handler the callback to handle the input.
     */
    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull Predicate<String> predicate, @Nonnull BiConsumer<Player, String> handler) {
        queue(plugin, p, new ChatInputHandler() {

            @Override
            public boolean test(String msg) {
                return predicate.test(msg);
            }

            @Override
            public void handleInput(Player p, String msg) {
                handler.accept(p, msg);
            }

        });
    }

    /**
     * Queue a callback for player input.
     * @param plugin the plugin instance.
     * @param p the player.
     * @param callback the callback to handle the input.
     */
    public static void queue(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull ChatInputHandler callback) {
        if (listener == null) {
            listener = new ChatInputListener(plugin);
        }

        listener.addCallback(p.getUniqueId(), callback);
    }
}