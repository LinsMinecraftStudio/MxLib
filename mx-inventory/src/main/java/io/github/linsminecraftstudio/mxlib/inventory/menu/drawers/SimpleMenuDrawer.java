package io.github.linsminecraftstudio.mxlib.inventory.menu.drawers;

import io.github.linsminecraftstudio.mxlib.inventory.menu.InvMenu;
import io.github.linsminecraftstudio.mxlib.inventory.menu.items.MxMenuItem;
import io.github.linsminecraftstudio.mxlib.inventory.menu.items.SimpleMenuItem;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SimpleMenuDrawer implements MenuDrawer {
    private final Map<Integer, MxMenuItem> items;

    public SimpleMenuDrawer() {
        this(new HashMap<>());
    }

    public SimpleMenuDrawer(Map<Integer, MxMenuItem> items) {
        this.items = items;
    }

    public SimpleMenuDrawer setItem(int slot, MxMenuItem item) {
        items.put(slot, item);
        return this;
    }

    public SimpleMenuDrawer setItem(int slot, ItemStack itemStack) {
        return setItem(slot, new SimpleMenuItem(null, itemStack));
    }

    @Override
    public void draw(InvMenu menu) {
        for (int i = 0; i < menu.getSize(); i++) {
            if (items.containsKey(i)) {
                menu.setItem(i, items.get(i));
            }
        }
    }
}
