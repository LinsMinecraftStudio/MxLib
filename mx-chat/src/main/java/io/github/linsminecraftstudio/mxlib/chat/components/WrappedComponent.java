package io.github.linsminecraftstudio.mxlib.chat.components;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * A wrapper for a Component provides more methods for sending and modifying the component.
 */
@Getter
public class WrappedComponent implements ComponentLike, Cloneable {
    private static final LegacyComponentSerializer SECTION = LegacyComponentSerializer.legacySection();
    private static final LegacyComponentSerializer AMPERSAND = LegacyComponentSerializer.legacyAmpersand();

    private static final WrappedComponent EMPTY = new WrappedComponent(Component.empty());
    private static final WrappedComponent RESET = new WrappedComponent(Component.empty().decoration(TextDecoration.ITALIC, false));

    private Component component;

    private WrappedComponent(Component component) {
        this.component = reset().append(component).getComponent();
    }

    private WrappedComponent(WrappedComponent component){
        this(component.component);
    }

    public static WrappedComponent empty() {
        return EMPTY.clone();
    }

    public static WrappedComponent reset() {
        return RESET.clone();
    }

    /**
     * Creates a WrappedComponent from a string.
     * @param text the text to create the component from
     * @return a {@link WrappedComponent}
     */
    public static WrappedComponent of(String text) {
        return of(text, true);
    }

    /**
     * Creates a WrappedComponent from a string.
     * @param text the text to create the component from
     * @param useAmpersand whether to use the legacy ampersand character or not
     * @return a {@link WrappedComponent}
     */
    public static WrappedComponent of(String text, boolean useAmpersand) {
        if (useAmpersand) {
            return new WrappedComponent(AMPERSAND.deserialize(text));
        } else {
            return new WrappedComponent(SECTION.deserialize(text));
        }
    }

    /**
     * Creates a WrappedComponent from a MiniMessage string.
     * @param text the text to create the component from
     * @return a {@link WrappedComponent}
     */
    public static WrappedComponent ofMiniMessage(String text) {
        return new WrappedComponent(MiniMessage.miniMessage().deserialize(text));
    }

    /**
     * Creates a WrappedComponent from a Component.
     * @param component the component to wrap
     * @return a {@link WrappedComponent}
     */
    public static WrappedComponent of(Component component) {
        return new WrappedComponent(component);
    }

    /**
     * Creates a WrappedComponent from a ComponentLike.
     * @param componentLike the component to wrap
     * @return a {@link WrappedComponent}
     */
    public static WrappedComponent of(ComponentLike componentLike) {
        return new WrappedComponent(componentLike.asComponent());
    }

    /**
     * Sends the component to a command sender.
     * @param sender the command sender to send the component to
     */
    public void send(CommandSender sender) {
        sender.sendMessage(component);
    }

    /**
     * Sends the component to a player as a title.
     * @param player the player to send the title to
     */
    public void sendAsTitle(Player player) {
        player.showTitle(Title.title(component, empty().asComponent()));
    }

    /**
     * Sends the component to a player as a title with a duration.
     * @param player the player to send the title to
     * @param fadein the duration of the title fade-in
     * @param stay the duration of the title stay
     * @param fadeout the duration of the title fade-out
     */
    public void sendAsTitle(Player player, Duration fadein, Duration stay, Duration fadeout) {
        player.showTitle(Title.title(component, empty().asComponent(), Title.Times.times(fadein, stay, fadeout)));
    }

    /**
     * Sends the component to a player as an action-bar message.
     * @param player the player to send the action-bar to
     */
    public void sendAsActionBar(Player player) {
        player.sendActionBar(component);
    }

    /**
     * Converts the component to a legacy text string (using ampersand characters).
     *
     * @return the legacy color text string
     */
    public String asLegacyText() {
        return asLegacyText(true);
    }

    /**
     * Converts the component to a legacy text string.
     * @param useAmpersand whether to use the legacy ampersand character or not
     * @return the legacy color text string
     */
    public String asLegacyText(boolean useAmpersand) {
        if (useAmpersand) {
            return AMPERSAND.serialize(component);
        } else {
            return SECTION.serialize(component);
        }
    }

    /**
     * Converts the component to a MiniMessage string.
     * @return the MiniMessage string
     */
    public String asMiniMessage() {
        return MiniMessage.miniMessage().serialize(component);
    }

    /**
     * Replaces all occurrences of a string with another string in the component.
     * @param oldText the old text to replace
     * @param newText the new text to replace with
     */
    public void replace(String oldText, String newText) {
        component = component.replaceText(b -> b.matchLiteral(oldText).replacement(newText));
    }

    /**
     * Replaces all occurrences of a string with another component in the component.
     * @param oldText the old text to replace
     * @param newComponent the new component to replace with
     */
    public WrappedComponent replace(String oldText, ComponentLike newComponent) {
        component = component.replaceText(b -> b.matchLiteral(oldText).replacement(newComponent));
        return this;
    }

    public WrappedComponent join(JoinConfiguration join, ComponentLike... components) {
        component = Component.join(join, components);
        return this;
    }

    /**
     * Appends a string to the end of the component.
     * @param text the string to append
     */
    public WrappedComponent append(String text) {
        component = component.append(Component.text(text));
        return this;
    }

    /**
     * Appends a component to the end of the component.
     * @param component the component to append
     */
    public WrappedComponent append(ComponentLike component) {
        this.component = this.component.append(component.asComponent());
        return this;
    }

    /**
     * Appends a legacy text string to the end of the component. (using ampersand characters)
     * @param text the legacy text string to append
     * @return the wrapped component
     */
    public WrappedComponent appendLegacy(String text) {
        return appendLegacy(text, true);
    }

    /**
     * Appends a legacy text string to the end of the component.
     * @param text the legacy text string to append
     * @param useAmpersand whether to use the legacy ampersand character or not
     * @return the wrapped component
     */
    public WrappedComponent appendLegacy(String text, boolean useAmpersand) {
        if (useAmpersand) {
            component = component.append(AMPERSAND.deserialize(text));
        } else {
            component = component.append(SECTION.deserialize(text));
        }

        return this;
    }

    /**
     * Appends a MiniMessage string to the end of the component.
     * @param text the MiniMessage string to append
     * @return the wrapped component
     */
    public WrappedComponent appendMiniMessage(String text) {
        component = component.append(MiniMessage.miniMessage().deserialize(text));
        return this;
    }

    /**
     * Sets the color of the component.
     * @param color the color to set
     * @return the wrapped component
     */
    public WrappedComponent color(NamedTextColor color) {
        component = component.color(color);
        return this;
    }

    /**
     * Sets the color of the component.
     * @param color the color to set
     * @return the wrapped component
     */
    public WrappedComponent color(int color) {
        component = component.color(TextColor.color(color));
        return this;
    }

    /**
     * Sets the color of the component.
     * @param color the color to set
     * @return the wrapped component
     */
    public WrappedComponent color(RGBLike color) {
        component = component.color(TextColor.color(color));
        return this;
    }

    /**
     * Sets the style of the component.
     * @param style the style to set
     * @return the wrapped component
     */
    public WrappedComponent style(Style style) {
        component = component.style(style);
        return this;
    }

    /**
     * Sets the display name of an item stack to the component.
     */
    public void asItemName(ItemStack item) {
        item.editMeta(meta -> meta.displayName(component));
    }

    @Override
    public @NotNull Component asComponent() {
        return component;
    }

    @Override
    public WrappedComponent clone() {
        return new WrappedComponent(component);
    }
}
