package io.github.linsminecraftstudio.mxlib.chat.components;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ComponentUtils {
    public static void sendClipboard(Player player, String message, String beingCopied) {
        sendClipboard(player, WrappedComponent.of(message), beingCopied);
    }

    public static void sendClipboard(Player player, ComponentLike message, String beingCopied) {
        Component component = message.asComponent();
        component = component.clickEvent(ClickEvent.copyToClipboard(beingCopied));
        player.sendMessage(component);
    }

    public static void sendHoverText(Player player, String message, String hoverText) {
        sendHoverText(player, WrappedComponent.of(message), hoverText);
    }

    public static void sendHoverText(Player player, ComponentLike message, String hoverText) {
        Component component = message.asComponent();
        component = component.hoverEvent(Component.text(hoverText));
        player.sendMessage(component);
    }

    public static Component getItemHoverText(ComponentLike message, ItemStack item) {
        Component component = message.asComponent();
        component = component.hoverEvent(item);
        return component;
    }

    public static Component getEntityHoverText(ComponentLike message, Entity entity) {
        Component component = message.asComponent();
        component = component.hoverEvent(entity);
        return component;
    }

    public static void addLoreToItem(ItemStack item, boolean appendEmptyLineFirst, ComponentLike... lore) {
        item.editMeta(meta -> {
            List<Component> originalLore = meta.lore();
            List<Component> loreList = new ArrayList<>();
            if (originalLore != null) {
                loreList.addAll(originalLore);
            }

            if (appendEmptyLineFirst) {
                loreList.add(Component.empty());
            }

            loreList.addAll(Stream.of(lore).map(ComponentLike::asComponent).toList());

            meta.lore(loreList);
        });
    }
}
