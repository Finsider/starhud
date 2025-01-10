package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class fps {

    public static Settings.FPSSettings fps = Main.settings.fpsSettings;

    private static final Identifier FPS_TEXTURE = Identifier.of("starhud", "hud/fps.png");

    public static void renderFPSHUD(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        String fpsStr = Integer.toString(client.getCurrentFps());

        int width = 59;
        int height = 13;

        int x = Helper.defaultHUDLocationX(fps.originX, context, width) + fps.x;
        int y = Helper.defaultHUDLocationY(fps.originY, context, height) + fps.y;

        int color = fps.color | 0xFF000000;

        context.drawTexture(RenderLayer::getGuiTextured, FPS_TEXTURE, x, y, 0.0F, 0.0F, width, height, width, height, color);
        context.drawText(client.textRenderer, fpsStr, x + 31, y + 3, color, false);
    }
}