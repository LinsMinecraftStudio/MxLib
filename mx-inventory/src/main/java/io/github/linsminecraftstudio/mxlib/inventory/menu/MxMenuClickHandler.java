package io.github.linsminecraftstudio.mxlib.inventory.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface MxMenuClickHandler {
    void onClick(int slot, InventoryClickEvent event, InvMenu self);
}
