package io.github.linsminecraftstudio.mxlib.inventory.menu;

import io.github.linsminecraftstudio.mxlib.inventory.menu.items.MxMenuItem;
import org.bukkit.inventory.InventoryHolder;

public abstract class MxInventoryHolder implements InventoryHolder {
    public abstract void setItem(int slot, MxMenuItem item);

    public abstract MxMenuItem getItem(int slot);
}
