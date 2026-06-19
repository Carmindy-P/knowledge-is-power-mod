package net.carmindy.kipmod.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class KIPModAutoConfig {
    public static KIPConfig CONFIG;

    public static void init() {
        AutoConfig.register(KIPConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(KIPConfig.class).getConfig();
    }
}