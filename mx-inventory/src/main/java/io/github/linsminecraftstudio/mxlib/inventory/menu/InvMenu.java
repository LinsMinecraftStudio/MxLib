package io.github.linsminecraftstudio.mxlib.inventory.menu;

import io.github.linsminecraftstudio.chat.components.WrappedComponent;
import io.github.linsminecraftstudio.mxlib.inventory.menu.drawers.MenuDrawer;
import io.github.linsminecraftstudio.mxlib.inventory.menu.items.MxMenuItem;
import lombok.Builder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class InvMenu extends MxInventoryHolder {

    @Builder.Default private Inventory baseInventory = null;
    @Builder.Default private WrappedComponent title = WrappedComponent.empty();
    @Builder.Default private int rows = -1;
    private JavaPlugin plugin;
    private MenuDrawer drawer;

    private final Map<Integer, MxMenuItem> items;

    private InvMenu() {
        if (this.plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        items = new HashMap<>();

        drawer.draw(this);
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (baseInventory == null) {
            initInventory();
        }

        return baseInventory;
    }

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
            rows = (int) Math.ceil(items.size() / 9.0);
        }

        rows = Math.min(rows, 6);
        baseInventory = Bukkit.createInventory(this, rows * 9, title.asComponent());
    }

    @Override
    @Nullable
    public MxMenuItem getItem(int slot) {
        return items.get(slot);
    }

    public int getSize() {
        if (baseInventory == null) {
            initInventory();
        }

        return baseInventory.getSize();
    }

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
