package fin.starhud;

import fin.starhud.config.Settings;
import fin.starhud.init.ConfigInit;
import fin.starhud.init.EventInit;
import fin.starhud.init.KeybindInit;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.option.KeyBinding;

public class Main implements ClientModInitializer {

    public static Settings settings;
    public static KeyBinding openEditHUDKey;

    @Override
    public void onInitializeClient() {
        ConfigInit.init();
        KeybindInit.init();
        EventInit.init();
    }

}
