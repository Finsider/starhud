package fin.starhud.hud.implementation;

import fin.starhud.config.hud.CoordSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public abstract class AbstractCoordinateHUD extends AbstractHUD {

    protected static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public final CoordSettings SETTINGS;
    public final Identifier TEXTURE;

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    public AbstractCoordinateHUD(CoordSettings coordSettings, Identifier TEXTURE) {
        super(coordSettings.base);

        this.SETTINGS = coordSettings;
        this.TEXTURE = TEXTURE;
    }

    public abstract int getCoord();

    private String coordStr;
    private int width;
    private int height;
    private int color;
    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        coordStr = Integer.toString(getCoord());
        int strWidth = CLIENT.textRenderer.getWidth(coordStr) - 1;

        displayMode = getSettings().getDisplayMode();

        width = displayMode.calculateWidth(ICON_WIDTH, strWidth);
        height = ICON_HEIGHT;

        color = SETTINGS.color | 0xFF000000;

        setWidthHeightColor(width, height, color);

        return true;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {

        int w = getWidth();
        int h = getHeight();

        RenderUtils.drawSmallHUD(
                context,
                coordStr,
                x, y,
                w, h,
                TEXTURE,
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
