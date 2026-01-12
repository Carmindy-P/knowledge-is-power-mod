package net.carmindy.kipmod.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Component to store enchantments on items in your mod system.
 */
public class ItemEnchantComponent {

    private final Map<String, Integer> enchantments = new HashMap<>();

    // Get an empty component (or in real usage, attach it to an ItemStack)
    public static ItemEnchantComponent get(Object itemStack) {
        // In your mod, this should fetch the component attached to the ItemStack
        // Here we just return a new instance for demonstration
        return new ItemEnchantComponent();
    }

    public boolean isEmpty() {
        return enchantments.isEmpty();
    }

    public Map<String, Integer> getEnchantments() {
        return Collections.unmodifiableMap(enchantments);
    }

    public void addEnchantment(String id, int level) {
        enchantments.put(id, level);
    }

    public void removeEnchantment(String id) {
        enchantments.remove(id);
    }
}
