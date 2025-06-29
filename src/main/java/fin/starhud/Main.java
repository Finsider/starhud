package fin.starhud;

import fin.starhud.config.Settings;
import fin.starhud.hud.HUDComponent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.ActionResult;

public class Main implements ClientModInitializer {

    public static Settings settings;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(Settings.class, GsonConfigSerializer::new);
        settings = AutoConfig.getConfigHolder(Settings.class).getConfig();
        AutoConfig.getConfigHolder(Settings.class).registerSaveListener(this::onConfigSaved);
    }

    private ActionResult onConfigSaved(ConfigHolder<Settings> holder, Settings config) {
        HUDComponent.getInstance().updateAll();
        return ActionResult.SUCCESS;
    }
}
