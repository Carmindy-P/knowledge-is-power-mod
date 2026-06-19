package net.carmindy.kipmod.component;

import net.carmindy.kipmod.abilities.Abilities;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;

public interface AbilityComponent extends Component {

    int getLevel();
    void setLevel(int level);

    @Nullable Abilities getAbility();

    void onXpGain(int orbValue);

    void setAbility(@Nullable Abilities ability);
    void tickCooldown();
    int getCooldown();
    void setCooldown(int ticks);

    boolean tryUseAbility();

    int getCharges();
    void setCharges(int charges);

    void setInstamine(boolean value);
    boolean isInstamine();
}