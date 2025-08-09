package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.FPSSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class FPSHUD extends AbstractHUD {

    private static final FPSSettings FPS_SETTINGS = Main.settings.fpsSettings;

    private static final Identifier FPS_TEXTURE = Identifier.of("starhud", "hud/fps.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public FPSHUD() {
        super(FPS_SETTINGS.base);
    }

    @Override
    public String getName() {
        return "FPS HUD";
    }

    @Override
    public String getId() {
        return HUDId.FPS.toString();
    }

    private String fpsStr;
    private int width;
    private int height;
    private int color;
    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        fpsStr = CLIENT.getCurrentFps() + " FPS";
        int strWidth = CLIENT.textRenderer.getWidth(fpsStr) - 1;

        displayMode = getSettings().getDisplayMode();

        width = displayMode.calculateWidth(ICON_WIDTH, strWidth);
        height = TEXTURE_HEIGHT;

        color = FPS_SETTINGS.color | 0xFF000000;

        setWidthHeightColor(width, height, color);

        return true;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {

        int w = getWidth();
        int h = getHeight();

        RenderUtils.drawSmallHUD(
                context,
                fpsStr,
                x, y,
                w, h,
                FPS_TEXTURE,
                0.0F, 0.0F,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                color,
                displayMode,
                drawBackground
        );
        return true;
    }
}
