package fin.starhud;

import fin.starhud.hud.HUDComponent;
import fin.starhud.init.*;
import net.fabricmc.api.ClientModInitializer;

public class FabricMain implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigInit.init();
        FabricKeybindInit.init();
        FabricEventInit.init();
        HUDComponent.getInstance().init();
    }

}
