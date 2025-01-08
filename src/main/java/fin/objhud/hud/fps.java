package fin.objhud.hud;

import fin.objhud.Helper;
import fin.objhud.Main;
import fin.objhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class fps {

    public static Settings.FPSSettings fps = Main.settings.fpsSettings;

    private static final Identifier FPS_TEXTURE = Identifier.of("objhud", "hud/fps.png");

    public static void renderFPSHUD(DrawContext context) {
        if (!fps.renderFPSHUD) return;

        MinecraftClient client = MinecraftClient.getInstance();
        String fpsStr = Integer.toString(client.getCurrentFps());

        int x = Helper.defaultHUDLocationX(fps.defX, context) + fps.x;
        int y = Helper.defaultHUDLocationY(fps.defY, context) + fps.y;

        int color = fps.color | 0xFF000000;

        Helper.drawTextureColor(context, FPS_TEXTURE, x, y, 0.0F, 0.0F, 56, 13, 56, 13, color);
        context.drawText(client.textRenderer, fpsStr, x + 28, y + 3, color, false);
    }
}