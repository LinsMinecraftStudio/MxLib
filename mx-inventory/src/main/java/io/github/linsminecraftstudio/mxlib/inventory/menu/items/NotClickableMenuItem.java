package io.github.linsminecraftstudio.mxlib.inventory.menu.items;

import org.bukkit.inventory.ItemStack;

public class NotClickableMenuItem extends SimpleMenuItem {
    public NotClickableMenuItem(ItemStack itemStack) {
        super((slot, event, self) -> event.setCancelled(true), itemStack);
    }
}
