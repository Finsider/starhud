package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.TPSSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.helper.TPSTracker;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class TPSHUD extends AbstractHUD {

    private static final TPSSettings SETTINGS = Main.settings.tpsSettings;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/tps.png");

    private static final int ICON_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = ICON_HEIGHT * 5;

    public TPSHUD() {
        super(SETTINGS.base);
    }

    private String str;
    private int color;
    private int step;
    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {

        double tps = TPSTracker.getTPS();

        step = getStep(tps);
        str = tps + SETTINGS.additionalString;
        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        displayMode = getSettings().getDisplayMode();
        int width = displayMode.calculateWidth(ICON_WIDTH, strWidth);

        color = (SETTINGS.useDynamicColor ? (AbstractDurabilityHUD.getItemBarColor(4 - step, 4)) : SETTINGS.color) | 0xFF000000;

        setWidthHeightColor(width, ICON_HEIGHT, color);

        return str != null;
    }

    public int getStep(double tps) {
        if (tps >= 19.9) return 0;
        else if (tps >= 19.5) return 1;
        else if (tps >= 18.0) return 2;
        else if (tps >= 15) return 3;
        else return 4;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {

        int w = getWidth();
        int h = getHeight();

        return RenderUtils.drawSmallHUD(
                context,
                str,
                x, y,
                w, h,
                TEXTURE,
                0.0F, ICON_HEIGHT * step,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                color,
                displayMode,
                drawBackground
        );
    }

    @Override
    public String getName() {
        return "TPS HUD";
    }

    @Override
    public String getId() {
        return HUDId.TPS.toString();
    }
}
