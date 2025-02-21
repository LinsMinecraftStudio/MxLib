package io.github.linsminecraftstudio.mxlib.inventory.menu.handlers;

import io.github.linsminecraftstudio.mxlib.inventory.menu.InvMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * An interface for handling the opening of an inventory menu.
 */
@FunctionalInterface
public interface MxMenuOpenHandler {
    MxMenuOpenHandler DEFAULT = (player, menu, event) -> {};

    void onOpen(Player player, InvMenu menu, InventoryOpenEvent event);
}
