package io.github.linsminecraftstudio.chat.components;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@Getter
public class WrappedComponent implements ComponentLike {
    private static final LegacyComponentSerializer SECTION = LegacyComponentSerializer.legacySection();
    private static final LegacyComponentSerializer AMPERSAND = LegacyComponentSerializer.legacyAmpersand();

    private final Component component;

    private WrappedComponent(Component component) {
        this.component = component;
    }

    public static WrappedComponent empty() {
        return new WrappedComponent(Component.empty());
    }

    public static WrappedComponent of(String text) {
        return of(text, true);
    }

    public static WrappedComponent of(String text, boolean useAmpersand) {
        if (useAmpersand) {
            return new WrappedComponent(AMPERSAND.deserialize(text));
        } else {
            return new WrappedComponent(SECTION.deserialize(text));
        }
    }

    public static WrappedComponent ofMiniMessage(String text) {
        return new WrappedComponent(MiniMessage.miniMessage().deserialize(text));
    }

    public static WrappedComponent of(Component component) {
        return new WrappedComponent(component);
    }

    public static WrappedComponent of(ComponentLike componentLike) {
        return new WrappedComponent(componentLike.asComponent());
    }

    public void send(CommandSender sender) {
        sender.sendMessage(component);
    }

    public void sendAsTitle(Player player) {
        player.showTitle(Title.title(component, empty().asComponent()));
    }

    public void sendAsTitle(Player player, Duration fadein, Duration stay, Duration fadeout) {
        player.showTitle(Title.title(component, empty().asComponent(), Title.Times.times(fadein, stay, fadeout)));
    }

    public void sendAsActionBar(Player player) {
        player.sendActionBar(component);
    }

    public String asLegacyText() {
        return asLegacyText(true);
    }

    public String asLegacyText(boolean useAmpersand) {
        if (useAmpersand) {
            return AMPERSAND.serialize(component);
        } else {
            return SECTION.serialize(component);
        }
    }

    public String asMiniMessage() {
        return MiniMessage.miniMessage().serialize(component);
    }

    @Override
    public @NotNull Component asComponent() {
        return component;
    }
}
