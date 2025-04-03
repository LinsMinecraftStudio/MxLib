package io.github.linsminecraftstudio.mxlib.inventory.menu.handlers;

import io.github.linsminecraftstudio.mxlib.inventory.menu.types.InvMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * An interface for handling the closing of an inventory menu.
 */
@FunctionalInterface
public interface MxMenuCloseHandler {
    MxMenuCloseHandler DEFAULT = (player, menu, event) -> {};

    void onClose(Player player, InvMenu menu, InventoryCloseEvent event);
}
