package io.github.linsminecraftstudio.mxlib.inventory.menu.filler;

import io.github.linsminecraftstudio.mxlib.inventory.menu.items.MxMenuItem;
import io.github.linsminecraftstudio.mxlib.inventory.menu.types.InvMenu;

public class MenuFiller {
    private final InvMenu menu;

    public MenuFiller(InvMenu menu) {
        this.menu = menu;
    }

    public void fill(FillType type, MxMenuItem item) {
        switch (type) {
            case FULL -> {
                for (int i = 0; i < menu.getSize(); i++) {
                    if (menu.getItem(i) == null) {
                        menu.setItem(i, item);
                    }
                }
            }
            case TOP_LINE -> {
                for (int i = 0; i < menu.getSize(); i++) {
                    if (i < 9) {
                        if (menu.getItem(i) == null) {
                            menu.setItem(i, item);
                        }
                    }
                }
            }
            case BOTTOM_LINE -> {
                for (int i = menu.getSize() - 9; i < 9; i++) {
                    if (menu.getItem(i) == null) {
                        menu.setItem(i, item);
                    }
                }
            }
            case NO_TOP_AND_BOTTOM -> {
                for (int i = 0; i < menu.getSize(); i++) {
                    if (i > 8 && i < menu.getSize() - 9) {
                        if (menu.getItem(i) == null) {
                            menu.setItem(i, item);
                        }
                    }
                }
            }
        }
    }
}
