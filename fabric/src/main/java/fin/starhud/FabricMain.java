package fin.starhud;

import fin.starhud.hud.HUDComponent;
import fin.starhud.init.ConfigInit;
import fin.starhud.init.EventInit;
import fin.starhud.init.KeybindInit;
import net.fabricmc.api.ClientModInitializer;

public class FabricMain implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigInit.init();
        KeybindInit.init();
        EventInit.init();
        HUDComponent.getInstance().init();
    }

}
