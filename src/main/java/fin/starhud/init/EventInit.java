package fin.starhud.init;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.hud.HUDComponent;
import fin.starhud.screen.EditHUDScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EventInit {

    private static final GeneralSettings.InGameHUDSettings SETTINGS = Main.settings.generalSettings.inGameSettings;

    public static void init() {

        // register keybinding event, on openEditHUDKey pressed -> move screen to edit hud screen.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Main.openEditHUDKey.wasPressed()) {
                client.setScreen(new EditHUDScreen(Text.of("Edit HUD"), client.currentScreen));
            }
        });

        // register hud element into before hotbar. I hope this was safe enough.
        HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, Identifier.of("starhud"), (context, tickCounter) -> {
            if (SETTINGS.disableHUDRendering) return;
            if (MinecraftClient.getInstance().options.hudHidden) return;
            if (!HUDComponent.getInstance().shouldRenderInGameScreen()) return;

            HUDComponent.getInstance().renderAll(context);
        });
    }
}
