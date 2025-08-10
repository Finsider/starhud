package fin.starhud.init;

import fin.starhud.Main;
import fin.starhud.config.Settings;
import fin.starhud.hud.HUDComponent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.util.ActionResult;

public class ConfigInit {
    public static void init() {

        AutoConfig.register(Settings.class, GsonConfigSerializer::new);
        ConfigHolder<Settings> holder = AutoConfig.getConfigHolder(Settings.class);
        Main.settings = holder.getConfig();

        // onConfigSaved we update every HUDs
        holder.registerSaveListener(ConfigInit::onConfigSaved);
    }

    public static ActionResult onConfigSaved(ConfigHolder<Settings> configHolder, Settings settings) {
        Main.settings.hudList.onConfigSaved();
        HUDComponent.getInstance().updateActiveHUDs();
        return ActionResult.SUCCESS;
    }
}
