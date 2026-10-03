package net.carmindy.kipmod.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ItemEnchantComponent {

    private final Map<String, Integer> enchantments = new HashMap<>();

    public static ItemEnchantComponent get(Object itemStack) {

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
