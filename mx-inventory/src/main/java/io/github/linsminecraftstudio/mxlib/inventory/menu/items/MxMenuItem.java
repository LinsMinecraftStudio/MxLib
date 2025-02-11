package io.github.linsminecraftstudio.mxlib.inventory.menu.items;

import io.github.linsminecraftstudio.mxlib.inventory.menu.MxMenuClickHandler;
import org.bukkit.inventory.ItemStack;

public interface MxMenuItem {
    MxMenuClickHandler getClickHandler();

    void setClickHandler(MxMenuClickHandler clickHandler);

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);
}
