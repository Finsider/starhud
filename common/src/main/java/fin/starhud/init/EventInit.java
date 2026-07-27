package fin.starhud.init;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.compat.ImmediatelyFastCompat;
import fin.starhud.config.GeneralSettings;
import fin.starhud.hud.HUDComponent;
import fin.starhud.screen.EditHUDScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class EventInit {

    private static final GeneralSettings.InGameHUDSettings SETTINGS = Main.settings.generalSettings.inGameSettings;

    public static void onOpenEditHUDKeyPressed(Minecraft client) {
        while (Main.openEditHUDKey.consumeClick()) {
            client.setScreen(new EditHUDScreen(Component.nullToEmpty("Edit HUD"), client.screen));
        }
    }

    public static void onToggleHUDKeyPressed(Minecraft client) {
        while (Main.toggleHUDKey.consumeClick()) {
            Main.settings.generalSettings.inGameSettings.disableHUDRendering = !Main.settings.generalSettings.inGameSettings.disableHUDRendering;
        }
    }

    public static void onHUDRender(GuiGraphics context, float tickCounter) {
        if (SETTINGS.disableHUDRendering) return;
        if (Minecraft.getInstance().options.hideGui) return;
        if (Minecraft.getInstance().screen instanceof EditHUDScreen) return;

        HUDComponent.getInstance().collectAll();

        if (SETTINGS.shouldBatchHUDWithImmediatelyFast && Helper.isModLoaded("immediatelyfast")) {
            ImmediatelyFastCompat.beginHudBatching();
            HUDComponent.getInstance().renderAll(context);
            ImmediatelyFastCompat.endHudBatching();
        } else {
            HUDComponent.getInstance().renderAll(context);
        }
    }


}
