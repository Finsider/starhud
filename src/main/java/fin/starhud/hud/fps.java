package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class fps {

    private static final Settings.FPSSettings fps = Main.settings.fpsSettings;
    private static final Settings.BaseSettings base = fps.base;

    private static final Identifier FPS_TEXTURE = Identifier.of("starhud", "hud/fps.png");

    private static final int width = 69;
    private static final int height = 13;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    private static int x;
    private static int y;

    static {
        initFPSConfiguration();
    }

    public static void renderFPSHUD(DrawContext context) {
        if (Helper.isHideOn(base.hideOn)) return;

        String fpsStr = client.getCurrentFps() + " FPS";

        int color = fps.color | 0xFF000000;

        Helper.renderHUD(context, base.scale, () -> {
            context.drawTexture(RenderLayer::getGuiTextured, FPS_TEXTURE, x, y, 0.0F, 0.0F, width, height, width, height, color);
            context.drawText(client.textRenderer, fpsStr, x + 19, y + 3, fps.color | 0xFF000000, false);
        });
    }

    public static void initFPSConfiguration() {
        x = Helper.calculatePositionX(base, width);
        y = Helper.calculatePositionY(base, height);
    }
}