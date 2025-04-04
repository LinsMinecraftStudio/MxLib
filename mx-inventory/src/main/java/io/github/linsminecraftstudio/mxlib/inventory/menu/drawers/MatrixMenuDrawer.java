package io.github.linsminecraftstudio.mxlib.inventory.menu.drawers;

import io.github.linsminecraftstudio.mxlib.inventory.menu.types.InvMenu;
import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuClickHandler;
import io.github.linsminecraftstudio.mxlib.inventory.menu.items.SimpleMenuItem;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides an easier way to create a menu by using a matrix of characters and items.
 * It allows adding click handlers to the items, which can be used to create interactive menus.
 * Use the {@link InvMenu.InvMenuBuilder#drawer(MenuDrawer)} method within
 * the {@link InvMenu.InvMenuBuilder#build()} method to process the menu drawer configuration.
 * <p>
 * Example usage:
 * <pre>
 * MatrixMenuDrawer drawer = new MatrixMenuDrawer(45)
 *     .addLine("BBBBBBBBB")
 *     .addLine("IIIIBOOOO")
 *     .addLine("IiiISOooO")
 *     .addLine("IIIIBOOOO")
 *     .addLine("BBBBBBBBB")
 *     .addExplain('B', MenuUtil.BACKGROUND, ClickHandler.DEFAULT)
 *     .addExplain('I', MenuUtil.INPUT_BORDER)
 *     .addExplain('O', MenuUtil.OUTPUT_BORDER);
 *
 * new MachineMenuPreset() {
 *     public void init() {
 *         addMenuDrawer(drawer);
 *     }
 * }
 * </pre>
 * <p>
 * In this example, a 45-size matrix is created with five lines of characters. When calling
 * {@link InvMenu#InvMenu()} to create a new menu, the drawer is passed to the menu constructor.
 * Method, characters 'B', 'I', 'O' are replaced with corresponding item stacks, and click handlers
 * are added to the menu.
 *
 * @author balugaq
 * @author lijinhong11
 */
@SuppressWarnings("unused")
@Getter
public final class MatrixMenuDrawer implements MenuDrawer {
    private final int size;
    private final Map<Character, ItemStack> charMap = new HashMap<>();
    private final Map<Character, MxMenuClickHandler> clickHandlerMap = new HashMap<>();
    private final List<String> matrix = new ArrayList<>();

    public MatrixMenuDrawer(@Range(from = 1, to = 54) int size) {
        this.size = size;
    }

    public MatrixMenuDrawer addLine(@NotNull String line) {
        matrix.add(line);
        return this;
    }

    public MatrixMenuDrawer addExplain(char c, @NotNull ItemStack itemStack) {
        charMap.put(c, new ItemStack(itemStack));
        return this;
    }

    public MatrixMenuDrawer addExplain(char c, @NotNull ItemStack itemStack, @NotNull MxMenuClickHandler clickHandler) {
        charMap.put(c, itemStack);
        clickHandlerMap.put(c, clickHandler);
        return this;
    }

    public MatrixMenuDrawer addClickHandler(char c, @NotNull MxMenuClickHandler clickHandler) {
        clickHandlerMap.put(c, clickHandler);
        return this;
    }

    public MatrixMenuDrawer addExplain(@NotNull String c, @NotNull ItemStack itemStack) {
        charMap.put(c.charAt(0), new ItemStack(itemStack));
        return this;
    }

    public MatrixMenuDrawer addExplain(@NotNull String c, @NotNull ItemStack itemStack, @NotNull MxMenuClickHandler clickHandler) {
        charMap.put(c.charAt(0), itemStack);
        clickHandlerMap.put(c.charAt(0), clickHandler);
        return this;
    }

    public MatrixMenuDrawer addClickHandler(@NotNull String c, @NotNull MxMenuClickHandler clickHandler) {
        clickHandlerMap.put(c.charAt(0), clickHandler);
        return this;
    }

    public int[] getCharPositions(@NotNull String s) {
        return getCharPositions(s.charAt(0));
    }

    public int[] getCharPositions(char c) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            String line = matrix.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == c) {
                    positions.add(i * 9 + j);
                }
            }
        }

        int[] result = new int[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            result[i] = positions.get(i);
        }
        return result;
    }

    public MatrixMenuDrawer clone() {
        MatrixMenuDrawer drawer = new MatrixMenuDrawer(size);
        drawer.charMap.putAll(charMap);
        drawer.clickHandlerMap.putAll(clickHandlerMap);
        drawer.matrix.addAll(matrix);
        return drawer;
    }

    @Override
    public void draw(InvMenu menu) {
        for (int i = 0; i < matrix.size(); i++) {
            String line = matrix.get(i);
            for (int j = 0; j < line.length(); j++) {
                int slot = i * 9 + j;
                if (slot >= size) {
                    continue;
                }

                char c = line.charAt(j);
                if (charMap.containsKey(c)) {
                    MxMenuClickHandler handler = clickHandlerMap.getOrDefault(c, null);
                    menu.setItem(slot, new SimpleMenuItem(handler, charMap.get(c)));
                }
            }
        }
    }
}