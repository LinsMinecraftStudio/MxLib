package io.github.linsminecraftstudio.mxlib.inventory.menu.types;

import io.github.linsminecraftstudio.mxlib.chat.components.WrappedComponent;
import io.github.linsminecraftstudio.mxlib.inventory.menu.drawers.SimpleMenuDrawer;
import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuCloseHandler;
import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuDragHandler;
import io.github.linsminecraftstudio.mxlib.inventory.menu.handlers.MxMenuOpenHandler;
import io.github.linsminecraftstudio.mxlib.inventory.menu.items.MxMenuItem;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A menu that can be paged. (linked with object lists)
 * <br><br>
 * <b>REMEMBER TO CALL {@link InvMenu#setupListener(Plugin)} BEFORE USING THE ENTIRE MENU SYSTEM</b>
 * @param <T> The type of the data to types.
 */
public abstract class PagedMenu<T> {
    private final Map<UUID, Integer> pageMap = new HashMap<>();
    private final Map<UUID, Map<Integer, CachedMenu>> menuCache = new ConcurrentHashMap<>();
    private List<T> data;
    private BukkitTask cacheCleanupTask;

    public PagedMenu(JavaPlugin plugin) {
        this(new ArrayList<>(), plugin);
    }

    public PagedMenu(List<T> data, JavaPlugin plugin) {
        this.data = data;
        startCacheCleanupTask(plugin); // Start the cache cleanup task
    }

    /**
     * Start a task to clean up expired cache entries every minute.
     * @param plugin The plugin instance to schedule the task.
     */
    private void startCacheCleanupTask(JavaPlugin plugin) {
        cacheCleanupTask = Bukkit.getScheduler().runTaskTimer(plugin, this::cleanupExpiredCache, 1200L, 1200L); // Run every 60 seconds (1200 ticks)
    }

    /**
     * Clean up cache entries that have not been accessed in the last minute.
     */
    private void cleanupExpiredCache() {
        long currentTime = System.currentTimeMillis();
        for (UUID playerId : menuCache.keySet()) {
            Map<Integer, CachedMenu> playerCache = menuCache.get(playerId);
            if (playerCache != null) {
                playerCache.entrySet().removeIf(entry -> {
                    CachedMenu cachedMenu = entry.getValue();
                    return currentTime - cachedMenu.getLastAccessTime() > 60000; // Remove if older than 1 minute
                });
                if (playerCache.isEmpty()) {
                    menuCache.remove(playerId); // Remove empty player cache
                }
            }
        }
    }

    /**
     * Stop the cache cleanup task.
     */
    public void stopCacheCleanupTask() {
        if (cacheCleanupTask != null) {
            cacheCleanupTask.cancel();
        }
    }

    /**
     * Get the slots that should be used for the content of the menu.
     * @return The slots that should be used for the content of the menu.
     */
    public abstract int[] getContentSlots();

    /**
     * Set up the menu for the given player and types. (called after {@link #getItem(T, int, int)})
     * @param player The player to set up the menu for.
     * @param menu The menu to set up.
     * @param page The types to set up the menu for. (starts at 1)
     */
    public abstract void setupForPage(Player player, InvMenu menu, int page);

    /**
     * Get the item for the given object on the given types and index. (called before {@link #setupForPage(Player, InvMenu, int)})
     * @param object The object to get the item for.
     * @param page The types to get the item for. (starts at 1)
     * @param index The index on the types to get the item for. (starts at 0)
     * @return The item for the given object on the given types and index.
     */
    public abstract MxMenuItem getItem(T object, int page, int index);

    /**
     * Get the title for the given types.
     * @param player The player to get the title for.
     * @param page The types to get the title for. (starts at 1)
     * @return The title for the given types.
     */
    public abstract Component getTitle(Player player, int page);

    protected MxMenuOpenHandler getMenuOpenHandler() {
        return MxMenuOpenHandler.DEFAULT;
    }

    protected MxMenuCloseHandler getMenuCloseHandler() {
        return MxMenuCloseHandler.DEFAULT;
    }

    protected MxMenuDragHandler getMenuDragHandler() {
        return MxMenuDragHandler.DEFAULT;
    }

    /**
     * Hook the given data list to the menu.
     * @param list The data list to hook.
     */
    public final void hookList(List<T> list) {
        this.data = list;
        menuCache.clear(); // Clear cache when data changes
    }

    public List<T> getDataList() {
        return data;
    }

    /**
     * Open the menu for the given player. (will automatically open the last opened types of the player if they have one)
     * @param player The player to open the menu for.
     */
    public void open(Player player) {
        render(player);
    }

    /**
     * Open the menu for the given player and types.
     * @param player The player to open the menu for.
     * @param page The types to open the menu for. (starts at 1)
     */
    public void open(Player player, int page) {
        jumpTo(player, page);
    }

    /**
     * Get the current types for the given player.
     * @param player The player to get the current types for.
     * @return The current types for the given player. (starts at 1)
     */
    public int getCurrentPage(Player player) {
        return pageMap.getOrDefault(player.getUniqueId(), 1);
    }

    protected void previous(Player player) {
        int currentPage = getCurrentPage(player);
        if (currentPage > 1) {
            pageMap.put(player.getUniqueId(), currentPage - 1);
            render(player);
        }
    }

    protected void next(Player player) {
        int currentPage = getCurrentPage(player);
        int maxPage = getMaxPage();
        if (currentPage < maxPage) {
            pageMap.put(player.getUniqueId(), currentPage + 1);
            render(player);
        }
    }

    protected void jumpTo(Player player, int page) {
        int maxPage = getMaxPage();
        if (page < 1) {
            page = 1;
        } else if (page > maxPage) {
            page = maxPage;
        }
        pageMap.put(player.getUniqueId(), page);
        render(player);
    }

    private int getMaxPage() {
        int itemsPerPage = getContentSlots().length;
        return (int) Math.ceil((double) data.size() / itemsPerPage);
    }

    private void render(Player player) {
        int page = getCurrentPage(player);
        UUID playerId = player.getUniqueId();

        CachedMenu cachedMenu = getCachedMenu(playerId, page);
        if (cachedMenu != null) {
            player.openInventory(cachedMenu.getMenu().getInventory());
            cachedMenu.updateLastAccessTime();
            return;
        }

        int itemsPerPage = getContentSlots().length;
        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, data.size());

        List<T> currentPageData = data.subList(startIndex, endIndex);

        SimpleMenuDrawer drawer = new SimpleMenuDrawer();

        for (int i = 0; i < currentPageData.size(); i++) {
            int slot = getContentSlots()[i];
            if (slot == -1 || i > 53) {
                continue;
            }

            drawer.setItem(slot, getItem(currentPageData.get(i), page, i));
        }

        InvMenu menu = InvMenu.builder()
                .title(WrappedComponent.of(getTitle(player, page)))
                .openHandler(getMenuOpenHandler())
                .closeHandler(getMenuCloseHandler())
                .dragHandler(getMenuDragHandler())
                .drawer(drawer)
                .build();

        setupForPage(player, menu, page);

        cacheMenu(playerId, page, menu);

        player.openInventory(menu.getInventory());
    }

    private CachedMenu getCachedMenu(UUID playerId, int page) {
        Map<Integer, CachedMenu> playerCache = menuCache.get(playerId);
        if (playerCache != null) {
            return playerCache.get(page);
        }
        return null;
    }

    private void cacheMenu(UUID playerId, int page, InvMenu menu) {
        Map<Integer, CachedMenu> playerCache = menuCache.computeIfAbsent(playerId, k -> new HashMap<>());
        playerCache.put(page, new CachedMenu(menu));
    }

    @Getter
    private static class CachedMenu {
        private final InvMenu menu;
        private long lastAccessTime;

        public CachedMenu(InvMenu menu) {
            this.menu = menu;
            this.lastAccessTime = System.currentTimeMillis();
        }

        public void updateLastAccessTime() {
            this.lastAccessTime = System.currentTimeMillis();
        }
    }
}