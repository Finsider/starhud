package fin.starhud;

import fin.starhud.hud.HUDComponent;
import fin.starhud.init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("starhud")
public class ForgeMain {

    public ForgeMain() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModContainer container = FMLJavaModLoadingContext.get().getContainer();

        ConfigInit.init();
        ForgeKeybindInit.init();
        ForgeEventInit.init(eventBus);

        ModListIntegration.registerModScreen(container);

        eventBus.addListener(ForgeKeybindInit::register);

    }
}