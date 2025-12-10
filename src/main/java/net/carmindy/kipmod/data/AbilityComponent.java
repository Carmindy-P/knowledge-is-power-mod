package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.Abilities;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;

/**
 * Interface defining a player's ability component.
 * Allows storing both the learned ability and its level.
 */
public interface AbilityComponent extends Component {

    int getLevel(); // Get the player's ability level
    void setLevel(int level); // Set the player's ability level

    @Nullable Abilities getAbility(); // Get the ability instance
    void setAbility(@Nullable Abilities ability); // Assign an ability to the player
}
