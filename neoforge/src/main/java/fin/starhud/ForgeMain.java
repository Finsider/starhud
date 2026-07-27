package fin.starhud;

import fin.starhud.hud.HUDComponent;
import fin.starhud.init.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value="starhud", dist = Dist.CLIENT)
public class ForgeMain {

    public ForgeMain(IEventBus eventBus, ModContainer container) {

        ConfigInit.init();
        NeoforgeKeybindInit.init();
        NeoforgeEventInit.init(eventBus);

        ModListIntegration.registerModScreen(container);

        eventBus.addListener(NeoforgeKeybindInit::register);

    }
}