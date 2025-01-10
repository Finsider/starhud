package fin.starhud;

import fin.starhud.config.Settings;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class Main implements ClientModInitializer {

    public static Settings settings;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(Settings.class, GsonConfigSerializer::new);
        settings = AutoConfig.getConfigHolder(Settings.class).getConfig();
    }
}
