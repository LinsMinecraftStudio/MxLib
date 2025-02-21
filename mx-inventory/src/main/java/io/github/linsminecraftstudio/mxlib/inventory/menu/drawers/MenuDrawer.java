package io.github.linsminecraftstudio.mxlib.inventory.menu.drawers;

import io.github.linsminecraftstudio.mxlib.inventory.menu.InvMenu;

public interface MenuDrawer {
    MenuDrawer EMPTY = menu -> {};

    void draw(InvMenu menu);
}
