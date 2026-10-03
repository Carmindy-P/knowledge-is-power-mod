package net.carmindy.kipmod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "kipmod")
public class KIPConfig implements ConfigData {

    @ConfigEntry.BoundedDiscrete(min = 0, max = 1000)
    public int globalCooldownTicks = 200;          // 10 s

    @Override
    public void validatePostLoad() {
        if (globalCooldownTicks < 10) globalCooldownTicks = 10;
    }
}