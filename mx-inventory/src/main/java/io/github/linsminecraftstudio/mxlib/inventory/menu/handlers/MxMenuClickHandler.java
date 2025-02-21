package io.github.linsminecraftstudio.mxlib.inventory.menu.handlers;

import io.github.linsminecraftstudio.mxlib.inventory.menu.InvMenu;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * An interface for handling menu clicks.
 */
@FunctionalInterface
public interface MxMenuClickHandler {
    void onClick(int slot, InventoryClickEvent event, InvMenu self);
}
