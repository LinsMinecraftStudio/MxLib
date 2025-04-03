package io.github.linsminecraftstudio.mxlib.inventory.menu.items;

import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuClickHandler;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class SimpleMenuItem implements MxMenuItem {
    @Nullable
    private MxMenuClickHandler onClick;
    private ItemStack itemStack;

    @Override
    @Nullable
    public MxMenuClickHandler getClickHandler() {
        return onClick;
    }

    @Override
    public void setClickHandler(MxMenuClickHandler onClick) {
        this.onClick = onClick;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public SimpleMenuItem clone() {
        return new SimpleMenuItem(onClick, itemStack.clone());
    }
}