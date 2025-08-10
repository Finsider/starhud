package fin.starhud;

import fin.starhud.config.Settings;
import fin.starhud.hud.HUDComponent;
import fin.starhud.init.ConfigInit;
import fin.starhud.init.EventInit;
import fin.starhud.init.KeybindInit;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.option.KeyBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("starhud");

    public static Settings settings;

    public static KeyBinding openEditHUDKey;
    public static KeyBinding toggleHUDKey;

    @Override
    public void onInitializeClient() {
        ConfigInit.init();
        KeybindInit.init();
        EventInit.init();
        HUDComponent.getInstance().init();
    }

}
