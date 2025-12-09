package net.carmindy.kipmod.data;

import net.carmindy.kipmod.abilities.Abilities;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;

public interface AbilityComponent extends Component {
    int getLevel();
    void setLevel(int level);

    @Nullable Abilities getAbility();
    void setAbility(@Nullable Abilities ability);
}
