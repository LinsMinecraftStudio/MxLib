package io.github.linsminecraftstudio.mxlib.inventory.menu.types;

import io.github.linsminecraftstudio.mxlib.inventory.menu.items.MxMenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

class InvMenuListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (event.getInventory().getHolder() instanceof InvMenu im) {
            MxMenuItem item = im.getItem(slot);
            if (item != null) {
                item.getClickHandler().onClick(slot, event, im);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof InvMenu im) {
            if (event.getPlayer() instanceof Player player) {
                im.getMenuOpenHandler().onOpen(player, im, event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof InvMenu im) {
            if (event.getPlayer() instanceof Player player) {
                im.getMenuCloseHandler().onClose(player, im, event);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof InvMenu im) {
            if (event.getWhoClicked() instanceof Player player) {
                im.getMenuDragHandler().onDrag(player, im, event);
            }
        }
    }
}
