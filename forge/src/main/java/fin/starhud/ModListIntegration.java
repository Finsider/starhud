package fin.starhud;

import fin.starhud.config.Settings;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;

public class ModListIntegration {

    public static void registerModScreen(ModContainer container) {
        container.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> AutoConfig.getConfigScreen(Settings.class, parent).get()
                )
        );
    }
}
