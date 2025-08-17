package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.SpeedSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class SpeedHUD extends AbstractHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/speed.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static final SpeedSettings SETTINGS = Main.settings.speedSettings;

    public SpeedHUD() {
        super(SETTINGS.base);
    }

    private String str;
    private int color;
    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {

        if (CLIENT.player == null) return false;

        Vec3d vel = CLIENT.player.getVelocity();

        double horizontalSpeed = (double) Math.round(vel.horizontalLength() * 20.0 * 10) / 10;

        str = horizontalSpeed + SETTINGS.additionalString;

        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        displayMode = getSettings().getDisplayMode();
        int width = displayMode.calculateWidth(ICON_WIDTH, strWidth);
        color = SETTINGS.color | 0xff000000;

        setWidthHeightColor(width, ICON_HEIGHT, color);

        return str != null;
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
                0.0F, 0.0F,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                color,
                displayMode,
                drawBackground
        );
    }

    @Override
    public String getName() {
        return "Speed HUD";
    }

    @Override
    public String getId() {
        return HUDId.SPEED.toString();
    }
}
