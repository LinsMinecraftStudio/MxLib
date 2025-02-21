package io.github.linsminecraftstudio.mxlib.inventory.items;

import com.destroystokyo.paper.Namespaced;
import io.github.linsminecraftstudio.mxlib.chat.components.WrappedComponent;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * An item stack editor that based builder-style class for editing item stacks easily.
 * <b>Note: it will clone the original item stack to avoid modifying the original one.</b>
 */
@SuppressWarnings({"unused", "deprecation"})
public class MxCustomItemStack {
    private final ItemStack itemStack;

    /* ItemStack-based constructors */
    public MxCustomItemStack(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public MxCustomItemStack(ItemStack itemStack, int amount) {
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(amount);
    }

    public MxCustomItemStack(ItemStack itemStack, int amount, int durability) {
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(amount);

        if (itemStack.getItemMeta() instanceof Damageable d) {
            d.setDamage(durability);
            this.itemStack.setItemMeta(d);
        }
    }
    
    public MxCustomItemStack(ItemStack itemStack, int amount, int durability, String displayName) {
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(amount);

        if (itemStack.getItemMeta() instanceof Damageable d) {
            d.setDamage(durability);
            this.itemStack.setItemMeta(d);
        }

        this.itemStack.editMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName)));
    }
    
    public MxCustomItemStack(ItemStack itemStack, int amount, String displayName) {
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(amount);

        this.itemStack.editMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName)));
    }
    
    public MxCustomItemStack(ItemStack itemStack, String displayName) {
        this.itemStack = itemStack.clone();

        this.itemStack.editMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName)));
    }
    
    public MxCustomItemStack(ItemStack itemStack, String displayName, String... lore) {
        this.itemStack = itemStack.clone();
        
        this.itemStack.editMeta(meta -> {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            if (lore.length > 0) {
                meta.setLore(Stream.of(lore).map(s -> ChatColor.translateAlternateColorCodes('&', s)).toList());
            }
        });
    }
    
    public MxCustomItemStack(ItemStack itemStack, int amount, String displayName, String... lore) {
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(amount);

        this.itemStack.editMeta(meta -> {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            if (lore.length > 0) {
                meta.setLore(Stream.of(lore).map(s -> ChatColor.translateAlternateColorCodes('&', s)).toList());
            }
        });
    }
    
    public MxCustomItemStack(ItemStack itemStack, int amount, int durability, String displayName, String... lore) {
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(amount);

        if (itemStack.getItemMeta() instanceof Damageable d) {
            d.setDamage(durability);
            this.itemStack.setItemMeta(d);
        }

        this.itemStack.editMeta(meta -> {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            if (lore.length > 0) {
                meta.setLore(Stream.of(lore).map(s -> ChatColor.translateAlternateColorCodes('&', s)).toList());
            }
        });
    }
    
    public MxCustomItemStack(ItemStack itemStack, int amount, int durability, ComponentLike displayName) {
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(amount);

        if (itemStack.getItemMeta() instanceof Damageable d) {
            d.setDamage(durability);
            this.itemStack.setItemMeta(d);
        }

        this.itemStack.editMeta(meta -> meta.displayName(displayName.asComponent()));
    }
    
    public MxCustomItemStack(ItemStack itemStack, int amount, ComponentLike displayName) {
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(amount);

        this.itemStack.editMeta(meta -> meta.displayName(displayName.asComponent()));
    }
    
    public MxCustomItemStack(ItemStack itemStack, ComponentLike displayName) {
        this.itemStack = itemStack.clone();

        this.itemStack.editMeta(meta -> meta.displayName(displayName.asComponent()));
    }
    
    public MxCustomItemStack(ItemStack itemStack, ComponentLike displayName, ComponentLike... lore) {
        this.itemStack = itemStack.clone();
        
        this.itemStack.editMeta(meta -> {
            meta.displayName(displayName.asComponent());
            if (lore.length > 0) {
                meta.lore(Stream.of(lore).map(ComponentLike::asComponent).toList());
            }
        });
    }

    /* Material-based constructors */
    public MxCustomItemStack(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public MxCustomItemStack(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public MxCustomItemStack(Material material, int amount, int durability) {
        this.itemStack = new ItemStack(material, amount);

        if (this.itemStack.getItemMeta() instanceof Damageable d) {
            d.setDamage(durability);
            this.itemStack.setItemMeta(d);
        }
    }
    
    public MxCustomItemStack(Material material, int amount, int durability, String displayName) {
        this.itemStack = new ItemStack(material, amount);

        if (this.itemStack.getItemMeta() instanceof Damageable d) {
            d.setDamage(durability);
            this.itemStack.setItemMeta(d);
        }

        this.itemStack.editMeta(meta -> meta.setDisplayName(displayName));
    }
    
    public MxCustomItemStack(Material material, int amount, String displayName) {
        this.itemStack = new ItemStack(material, amount);

        this.itemStack.editMeta(meta -> meta.setDisplayName(displayName));
    }
    
    public MxCustomItemStack(Material material, String displayName) {
        this.itemStack = new ItemStack(material);

        this.itemStack.editMeta(meta -> meta.setDisplayName(displayName));
    }
    
    public MxCustomItemStack(Material material, String displayName, String... lore) {
        this.itemStack = new ItemStack(material);
        
        this.itemStack.editMeta(meta -> {
            meta.setDisplayName(displayName);
            if (lore.length > 0) {
                meta.setLore(Stream.of(lore).toList());
            }
        });
    }
    
    public MxCustomItemStack(Material material, int amount, String displayName, String... lore) {
        this.itemStack = new ItemStack(material, amount);

        this.itemStack.editMeta(meta -> {
            meta.setDisplayName(displayName);
            if (lore.length > 0) {
                meta.setLore(Stream.of(lore).toList());
            }
        });
    }
    
    public MxCustomItemStack(Material material, int amount, int durability, String displayName, String... lore) {
        this.itemStack = new ItemStack(material, amount);

        if (this.itemStack.getItemMeta() instanceof Damageable d) {
            d.setDamage(durability);
            this.itemStack.setItemMeta(d);
        }

        this.itemStack.editMeta(meta -> {
            meta.setDisplayName(displayName);
            if (lore.length > 0) {
                meta.setLore(Stream.of(lore).toList());
            }
        });
    }
    
    public MxCustomItemStack(Material material, int amount, int durability, ComponentLike displayName) {
        this.itemStack = new ItemStack(material, amount);

        if (this.itemStack.getItemMeta() instanceof Damageable d) {
            d.setDamage(durability);
            this.itemStack.setItemMeta(d);
        }

        this.itemStack.editMeta(meta -> meta.displayName(displayName.asComponent()));
    }
    
    public MxCustomItemStack(Material material, int amount, ComponentLike displayName) {
        this.itemStack = new ItemStack(material, amount);

        this.itemStack.editMeta(meta -> meta.displayName(displayName.asComponent()));
    }
    
    public MxCustomItemStack(Material material, ComponentLike displayName) {
        this.itemStack = new ItemStack(material);

        this.itemStack.editMeta(meta -> meta.displayName(displayName.asComponent()));
    }
    
    public MxCustomItemStack(Material material, ComponentLike displayName, ComponentLike... lore) {
        this.itemStack = new ItemStack(material);
        
        this.itemStack.editMeta(meta -> {
            meta.displayName(displayName.asComponent());
            if (lore.length > 0) {
                meta.lore(Stream.of(lore).map(ComponentLike::asComponent).toList());
            }
        });
    }

    /* Setter methods */
    public MxCustomItemStack setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public MxCustomItemStack setDurability(int durability) {
        if (itemStack.getItemMeta() instanceof Damageable d) {
            d.setDamage(durability);
            itemStack.setItemMeta(d);
        }
        return this;
    }

    public MxCustomItemStack setDisplayName(String displayName) {
        itemStack.editMeta(meta -> meta.setDisplayName(displayName));
        return this;
    }

    public MxCustomItemStack displayName(ComponentLike displayName) {
        itemStack.editMeta(meta -> meta.displayName(displayName.asComponent()));
        return this;
    }

    public MxCustomItemStack setLore(String... lore) {
        itemStack.editMeta(meta -> meta.setLore(Stream.of(lore).toList()));
        return this;
    }

    public MxCustomItemStack setLore(List<String> lore) {
        itemStack.editMeta(meta -> meta.setLore(lore));
        return this;
    }

    public MxCustomItemStack lore(ComponentLike... lore) {
        itemStack.editMeta(meta -> meta.lore(Stream.of(lore).map(ComponentLike::asComponent).toList()));
        return this;
    }

    public MxCustomItemStack lore(List<ComponentLike> lore) {
        itemStack.editMeta(meta -> meta.lore(lore.stream().map(ComponentLike::asComponent).toList()));
        return this;
    }

    public MxCustomItemStack setItemMeta(ItemMeta meta) {
        itemStack.setItemMeta(meta);
        return this;
    }

    public MxCustomItemStack setItemMeta(Consumer<ItemMeta> consumer) {
        itemStack.editMeta(consumer);
        return this;
    }

    public MxCustomItemStack setUnbreakable(boolean unbreakable) {
        itemStack.editMeta(meta -> meta.setUnbreakable(unbreakable));
        return this;
    }

    public MxCustomItemStack enchant(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public MxCustomItemStack removeEnchantment(Enchantment enchantment) {
        itemStack.removeEnchantment(enchantment);
        return this;
    }

    public MxCustomItemStack addItemFlags(ItemFlag... flags) {
        itemStack.addItemFlags(flags);
        return this;
    }

    public MxCustomItemStack removeItemFlags(ItemFlag... flags) {
        itemStack.removeItemFlags(flags);
        return this;
    }

    public MxCustomItemStack setModelData(int data) {
        itemStack.editMeta(meta -> meta.setCustomModelData(data));
        return this;
    }

    public MxCustomItemStack addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        itemStack.editMeta(meta -> meta.addAttributeModifier(attribute, modifier));
        return this;
    }

    public MxCustomItemStack removeAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        itemStack.editMeta(meta -> meta.removeAttributeModifier(attribute, modifier));
        return this;
    }

    public MxCustomItemStack setPlaceableKeys(Collection<Namespaced> placeableKeys) {
        itemStack.editMeta(meta -> meta.setPlaceableKeys(placeableKeys));
        return this;
    }

    public MxCustomItemStack setPlaceableKeys(Namespaced... placeableKeys) {
        itemStack.editMeta(meta -> meta.setPlaceableKeys(List.of(placeableKeys)));
        return this;
    }

    public MxCustomItemStack setDestroyableKeys(Collection<Namespaced> destroyableKeys) {
        itemStack.editMeta(meta -> meta.setDestroyableKeys(destroyableKeys));
        return this;
    }

    public MxCustomItemStack setDestroyableKeys(Namespaced... destroyableKeys) {
        itemStack.editMeta(meta -> meta.setDestroyableKeys(List.of(destroyableKeys)));
        return this;
    }

    /* Getter methods */
    public Material getType() {
        return itemStack.getType();
    }

    public int getAmount() {
        return itemStack.getAmount();
    }

    /**
     * Gets the durability of the item stack.
     * @return the durability of the item stack or {@code -32768} if the item stack isn't damageable
     */
    public int getDurability() {
        if (itemStack.getItemMeta() instanceof Damageable d) {
            return d.getDamage();
        }

        return -32768;
    }

    public String getDisplayName() {
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return "";
        }

        return itemStack.getItemMeta().getDisplayName();
    }

    /**
     * Gets the display name component safely (empty component if no display name is present).
     * @return the display name component or an empty component if no display name is present
     */
    public WrappedComponent getDisplayNameComponent() {
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return WrappedComponent.empty();
        }

        return WrappedComponent.of(itemStack.getItemMeta().displayName());
    }

    /**
     * Returns the display name component with fallback to the internal display name (if present).
     * @return the display name component with fallback to the internal display name (if present)
     */
    public WrappedComponent getDisplayNameComponentI18nFallback() {
        return WrappedComponent.of(itemStack.displayName());
    }

    /**
     * Gets the lore list safely (empty list if no lore is present).
     * @return the lore or an empty list if no lore is present
     */
    public List<String> getLore() {
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) {
            return List.of();
        }

        return itemStack.getItemMeta().getLore();
    }

    /**
     * Gets the lore list safely (empty list if no lore is present).
     * @return the lore or an empty list if no lore is present
     */
    public List<WrappedComponent> getLoreComponents() {
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) {
            return List.of();
        }

        return itemStack.getItemMeta().lore().stream().map(WrappedComponent::of).toList();
    }

    /**
     * Gets the built item stack.
     * @return the built item stack
     */
    public ItemStack asBukkit() {
        return itemStack;
    }
}
