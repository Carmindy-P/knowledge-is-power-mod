package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.Abilities;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;

public interface AbilityComponent extends Component {

    int getLevel(); // Get the player's ability level
    void setLevel(int level); // Set the player's ability level

    @Nullable Abilities getAbility(); // Get the ability instance
    void setAbility(@Nullable Abilities ability); // Assign an ability to the player
    void tickCooldown();
    int getCooldown();
    void setCooldown(int ticks);

    boolean tryUseAbility();

    void setInstamine(boolean value); // Set the instamine flag
    boolean isInstamine(); // Check if instamine is active
}