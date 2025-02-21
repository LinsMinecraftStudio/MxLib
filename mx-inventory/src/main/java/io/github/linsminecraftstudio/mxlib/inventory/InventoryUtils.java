package io.github.linsminecraftstudio.mxlib.inventory;

import io.github.linsminecraftstudio.mxlib.chat.components.WrappedComponent;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class InventoryUtils {
    public static Inventory getNewCopy(Inventory from, String title) {
        return getNewCopy(from, WrappedComponent.of(title));
    }

    public static Inventory getNewCopy(Inventory from, ComponentLike title) {
        Inventory to = Bukkit.createInventory(null, from.getSize(), title.asComponent());
        copy(from, to);
        return to;
    }

    public static void copy(Inventory from, Inventory to) {
        if (from.getSize() > to.getSize()) {
            throw new IllegalArgumentException("origin inventory is larger than the destination inventory");
        }

        for (int i = 0; i < from.getSize(); i++) {
            to.setItem(i, from.getItem(i));
        }

        to.setMaxStackSize(from.getMaxStackSize());
    }
}
