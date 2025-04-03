package io.github.linsminecraftstudio.mxlib.inventory.menu.drawers;

import io.github.linsminecraftstudio.mxlib.inventory.items.MxCustomItemStack;
import io.github.linsminecraftstudio.mxlib.inventory.menu.types.InvMenu;
import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuClickHandler;
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

    public SimpleMenuDrawer setItem(int slot, MxCustomItemStack itemStack) {
        return setItem(slot, new SimpleMenuItem(null, itemStack.asBukkit()));
    }

    public SimpleMenuDrawer setItem(int slot, ItemStack itemStack, MxMenuClickHandler clickHandler) {
        return setItem(slot, new SimpleMenuItem(clickHandler, itemStack));
    }

    public SimpleMenuDrawer setItem(int slot, MxCustomItemStack itemStack, MxMenuClickHandler clickHandler) {
        return setItem(slot, new SimpleMenuItem(clickHandler, itemStack.asBukkit()));
    }

    @Override
    public void draw(InvMenu menu) {
        for (int i = 0; i < menu.getSize(); i++) {
            if (items.containsKey(i)) {
                menu.setItem(i, items.get(i));
            }
        }
    }

    @Override
    public SimpleMenuDrawer clone(){
        return new SimpleMenuDrawer(new HashMap<>(items));
    }
}
