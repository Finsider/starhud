package fin.starhud.init;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.hud.HUDComponent;
import fin.starhud.integration.ImmediatelyFastAPI;
import fin.starhud.screen.EditHUDScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class EventInit {

    private static final GeneralSettings.InGameHUDSettings SETTINGS = Main.settings.generalSettings.inGameSettings;;

    public static void init() {

        // register keybinding event, on openEditHUDKey pressed -> move screen to edit hud screen.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Main.openEditHUDKey.wasPressed()) {
                client.setScreen(new EditHUDScreen(Text.of("Edit HUD"), client.currentScreen));
            }
        });

        // register hud... idk where tho haha
        HudRenderCallback.EVENT.register( (context, tickCounter) -> {
            if (SETTINGS.disableHUDRendering) return;

            if (SETTINGS.shouldBatchHUDWithImmediatelyFast && ImmediatelyFastAPI.isModPresent()) {
                ImmediatelyFastAPI.beginHudBatching();
                if (!MinecraftClient.getInstance().options.hudHidden)
                    if (HUDComponent.getInstance().shouldRenderInGameScreen())
                        HUDComponent.getInstance().renderAll(context);
                ImmediatelyFastAPI.endHudBatching();
            } else {
                if (!MinecraftClient.getInstance().options.hudHidden)
                    if (HUDComponent.getInstance().shouldRenderInGameScreen())
                        HUDComponent.getInstance().renderAll(context);
            }
        });
    }
}
