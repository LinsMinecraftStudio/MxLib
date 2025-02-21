package io.github.linsminecraftstudio.mxlib.inventory.menu.items;

import io.github.linsminecraftstudio.mxlib.inventory.items.MxCustomItemStack;
import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuClickHandler;
import org.bukkit.inventory.ItemStack;

public interface MxMenuItem {
    MxMenuClickHandler getClickHandler();

    void setClickHandler(MxMenuClickHandler clickHandler);

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);

    default void setItemStack(MxCustomItemStack itemStack) {
        setItemStack(itemStack.asBukkit());
    }
}
