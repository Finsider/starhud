package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.FPSSettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class FPS extends AbstractHUD {

    private static final FPSSettings FPS_SETTINGS = Main.settings.fpsSettings;

    private static final Identifier FPS_TEXTURE = Identifier.of("starhud", "hud/fps.png");

    private static final int TEXTURE_WIDTH = 69;
    private static final int TEXTURE_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public FPS() {
        super(FPS_SETTINGS.base);
    }

    @Override
    public String getName() {
        return "FPS HUD";
    }

    @Override
    public boolean renderHUD(DrawContext context) {
        String fpsStr = CLIENT.getCurrentFps() + " FPS";
        int color = FPS_SETTINGS.color | 0xFF000000;

        RenderUtils.drawTextureHUD(context, FPS_TEXTURE, x, y, 0.0F, 0.0F, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT, color);
        RenderUtils.drawTextHUD(context, fpsStr, x + 19, y + 3, color, false);

        setBoundingBox(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT, color);
        return true;
    }

    @Override
    public int getBaseHUDWidth() {
        return TEXTURE_WIDTH;
    }

    @Override
    public int getBaseHUDHeight() {
        return TEXTURE_HEIGHT;
    }
}
