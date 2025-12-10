package net.carmindy.kipmod;

import net.carmindy.kipmod.abilities.ModAbilities;
import net.carmindy.kipmod.items.AbilityBookFactory;
import net.fabricmc.api.ModInitializer;
import net.carmindy.kipmod.events.BookUseHandler;
import net.minecraft.item.ItemStack;

public class KnowledgeIsPowerMod implements ModInitializer {

    public static final String MOD_ID = "knowledge-is-power-mod";

    @Override
    public void onInitialize() {
        ModAbilities.register();
        BookUseHandler.registerHandler();
        ItemStack flameBook = AbilityBookFactory.createAbilityBook(ModAbilities.FLAME);
    }

}
