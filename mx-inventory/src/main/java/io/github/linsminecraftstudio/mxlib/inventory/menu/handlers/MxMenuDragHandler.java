package io.github.linsminecraftstudio.mxlib.inventory.menu.handlers;

import io.github.linsminecraftstudio.mxlib.inventory.menu.InvMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;

@FunctionalInterface
public interface MxMenuDragHandler {
    MxMenuDragHandler DEFAULT = (player, self, event) -> {};

    void onDrag(Player player, InvMenu self, InventoryDragEvent event);
}
