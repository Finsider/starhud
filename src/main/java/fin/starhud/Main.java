package fin.starhud;

import fin.starhud.config.Settings;
import fin.starhud.hud.HUDComponent;
import fin.starhud.screen.EditHUDScreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

public class Main implements ClientModInitializer {

    public static Settings settings;
    public static KeyBinding openEditHUDKey;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(Settings.class, GsonConfigSerializer::new);
        settings = AutoConfig.getConfigHolder(Settings.class).getConfig();
        AutoConfig.getConfigHolder(Settings.class).registerSaveListener(this::onConfigSaved);

        openEditHUDKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.starhud.open_edithud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.starhud"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openEditHUDKey.wasPressed()) {
                client.setScreen(new EditHUDScreen(Text.of("Edit HUD"), client.currentScreen));
            }
        });
    }

    private ActionResult onConfigSaved(ConfigHolder<Settings> holder, Settings config) {
        HUDComponent.getInstance().updateAll();
        return ActionResult.SUCCESS;
    }
}
