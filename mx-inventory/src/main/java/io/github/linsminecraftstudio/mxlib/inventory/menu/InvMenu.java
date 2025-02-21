package io.github.linsminecraftstudio.mxlib.inventory.menu;

import io.github.linsminecraftstudio.mxlib.chat.components.WrappedComponent;
import io.github.linsminecraftstudio.mxlib.inventory.InventoryUtils;
import io.github.linsminecraftstudio.mxlib.inventory.menu.drawers.MenuDrawer;
import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuCloseHandler;
import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuDragHandler;
import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuOpenHandler;
import io.github.linsminecraftstudio.mxlib.inventory.menu.items.MxMenuItem;
import lombok.Builder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class InvMenu extends MxInventoryHolder {

    /**
     * CALL THIS METHOD BEFORE CREATING MENUS
     * @param plugin the plugin to register the listener to
     */
    public static void setupListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new InvMenuListener(), plugin);
    }

    /* builder fields */
    @Builder.Default private Inventory baseInventory = null;
    @Builder.Default private WrappedComponent title = WrappedComponent.empty();
    @Builder.Default private MxMenuOpenHandler openHandler = MxMenuOpenHandler.DEFAULT;
    @Builder.Default private MxMenuCloseHandler closeHandler = MxMenuCloseHandler.DEFAULT;
    @Builder.Default private MxMenuDragHandler dragHandler = MxMenuDragHandler.DEFAULT;
    @Builder.Default private int rows = -1;
    @Builder.Default private boolean copyBaseInventory = true;
    @Builder.Default private MenuDrawer drawer = MenuDrawer.EMPTY;

    /* Internal fields */
    private final Map<Integer, MxMenuItem> items = new HashMap<>();

    InvMenu(Inventory baseInventory, WrappedComponent title, MxMenuOpenHandler openHandler, MxMenuCloseHandler closeHandler, MxMenuDragHandler dragHandler, int rows, boolean copyBaseInventory, MenuDrawer drawer) {
        this.baseInventory = baseInventory;
        this.title = title;
        this.openHandler = openHandler;
        this.closeHandler = closeHandler;
        this.dragHandler = dragHandler;
        this.rows = rows;
        this.copyBaseInventory = copyBaseInventory;
        this.drawer = drawer;

        if (this.openHandler == null) {
            this.openHandler = MxMenuOpenHandler.DEFAULT;
        }

        if (this.closeHandler == null) {
            this.closeHandler = MxMenuCloseHandler.DEFAULT;
        }

        if (this.drawer == null) {
            this.drawer = MenuDrawer.EMPTY;
        }

        this.drawer.draw(this);
    }

    /**
     * Gets the bukkit inventory
     * @return the bukkit inventory
     */
    @Override
    public @NotNull Inventory getInventory() {
        if (baseInventory == null) {
            initInventory();
        }

        return baseInventory;
    }

    public @NotNull Inventory getInventoryWithoutHolder() {
        if (baseInventory == null) {
            initInventory();
        }

        return InventoryUtils.getNewCopy(baseInventory, title);
    }

    /**
     * Sets the item at the specified slot.
     * @param slot the slot to set the item at
     * @param item the item to set, or null to remove the item
     */
    @Override
    public void setItem(int slot, @Nullable MxMenuItem item) {
        items.put(slot, item);

        if (baseInventory == null) {
            initInventory();
        }

        baseInventory.setItem(slot, item == null ? null : item.getItemStack());

        for (Player player : getViewers()) {
            player.updateInventory();
        }
    }

    private void initInventory() {
        if (rows < 0) {
            if (items.isEmpty()) {
                rows = 1;
            } else {
                rows = (int) Math.ceil(items.size() / 9.0);

                if (rows > 6) {
                    throw new IllegalArgumentException("Too many rows for menu, the maximum is 6");
                }

                if (rows * 9 < items.size()) {
                    rows++;
                }
            }
        }

        rows = Math.min(rows, 6);

        if (copyBaseInventory) {
            if (baseInventory != null) {
                baseInventory = InventoryUtils.getNewCopy(baseInventory, title);
            } else {
                baseInventory = Bukkit.createInventory(this, rows * 9, title.asComponent());
            }
        } else {
            baseInventory = Bukkit.createInventory(this, rows * 9, title.asComponent());
        }
    }

    /**
     * Gets the item at the specified slot.
     * @param slot the slot to get the item from
     * @return the item at the specified slot, or null if there is no item at that slot
     */
    @Override
    @Nullable
    public MxMenuItem getItem(int slot) {
        return items.get(slot);
    }

    /**
     * Gets the size of the inventory.
     * @return the size of the inventory
     */
    public int getSize() {
        if (baseInventory == null) {
            initInventory();
        }

        return baseInventory.getSize();
    }

    public MxMenuOpenHandler getMenuOpenHandler() {
        return openHandler;
    }

    public MxMenuCloseHandler getMenuCloseHandler() {
        return closeHandler;
    }

    public MxMenuDragHandler getMenuDragHandler() {
        return dragHandler;
    }

    /**
     * Get who is viewing the inventory.
     * @return a list of players who are viewing the inventory (excluding human entities)
     */
    public List<Player> getViewers() {
        if (baseInventory == null) {
            initInventory();
            return new ArrayList<>();
        }

        return baseInventory.getViewers()
                .stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .toList();
    }
}
