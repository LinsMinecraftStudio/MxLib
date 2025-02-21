package io.github.linsminecraftstudio.mxlibexample;

import io.github.linsminecraftstudio.mxlib.chat.components.WrappedComponent;
import io.github.linsminecraftstudio.mxlib.inventory.items.MxCustomItemStack;
import io.github.linsminecraftstudio.mxlib.inventory.menu.InvMenu;
import io.github.linsminecraftstudio.mxlib.inventory.menu.drawers.MatrixMenuDrawer;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Part 1: Inventory System Setup & Inventory Creation
        InvMenu.setupListener(this);

        MatrixMenuDrawer menuDrawer = new MatrixMenuDrawer(54)
                .addLine("EEEEEEEEE")
                .addLine("E       E")
                .addLine("E       E")
                .addLine("E       E")
                .addLine("E       E")
                .addLine("EEEEEEEEE")
                .addExplain('E', new MxCustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").asBukkit());

        InvMenu menu = InvMenu.builder()
                .title(WrappedComponent.of("Example Menu"))
                .rows(6)
                .drawer(menuDrawer)

                //Add it if you want

                .closeHandler((player, self, event) -> {
                    player.sendMessage(WrappedComponent.of("Menu closed!").color(NamedTextColor.GREEN));
                })

                .openHandler((player, self, event) -> {
                    player.sendMessage(WrappedComponent.of("Menu opened!").color(NamedTextColor.GREEN));
                })

                .dragHandler((player, self, event) -> {
                    player.sendMessage(WrappedComponent.of("You dragged something!").color(NamedTextColor.GREEN));
                })

                //.copyBaseInventory(false)
                //.baseInventory(null)

                .build();

        getServer().getOnlinePlayers().stream().findFirst().ifPresent(any -> {
            any.openInventory(menu.getInventory());
        });

        // Part 2: ...
    }
}
