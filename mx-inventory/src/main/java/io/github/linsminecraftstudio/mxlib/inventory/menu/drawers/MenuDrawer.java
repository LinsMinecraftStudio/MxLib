package io.github.linsminecraftstudio.mxlib.inventory.menu.drawers;

import io.github.linsminecraftstudio.mxlib.inventory.menu.types.InvMenu;

public interface MenuDrawer extends Cloneable {
    MenuDrawer EMPTY = menu -> {};

    void draw(InvMenu menu);
}
