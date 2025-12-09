package net.carmindy.kipmod.items;

import net.carmindy.kipmod.abilities.Abilities;
import net.carmindy.kipmod.data.AbilityBookComponent;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AbilityBookFactory {

    public static ItemStack createAbilityBook(Abilities ability) {
        // Use Items.ENCHANTED_BOOK directly
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        AbilityBookComponent.setAbility(book, ability.getId());
        return book;
    }
}
