package fin.starhud;

import fin.starhud.config.Settings;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ModListIntegration {

    public static void registerModScreen(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (client, parent) -> AutoConfig
                        .getConfigScreen(Settings.class, parent)
                        .get()
        );
    }
}
