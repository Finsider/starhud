package fin.starhud;

import fin.starhud.config.Settings;
import fin.starhud.hud.*;

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
        armor.initArmorConfiguration();
        biome.initBiomeConfiguration();
        clock.initClockSystemConfiguration();
        clock.initClockInGameConfiguration();
        coordinate.initCoordinateConfiguration();
        direction.initDirectionConfiguration();
        hand.initLeftHandConfiguration();
        hand.initRightHandConfiguration();
        inventory.initInventoryConfiguration();
        ping.initPingConfiguration();

        return ActionResult.SUCCESS;
    }
}
