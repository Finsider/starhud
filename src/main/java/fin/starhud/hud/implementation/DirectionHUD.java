package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.DirectionSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class DirectionHUD extends AbstractHUD {

    private static final DirectionSettings DIRECTION_SETTINGS = Main.settings.directionSettings;

    private static final Identifier DIRECTION_CARDINAL_TEXTURE = Identifier.of("starhud", "hud/direction.png");
    private static final Identifier DIRECTION_ORDINAL_TEXTURE = Identifier.of("starhud", "hud/direction_ordinal.png");

    private static final int ORDINAL_TEXTURE_WIDTH = 19;
    private static final int ORDINAL_TEXTURE_HEIGHT = 104;
    private static final int ORDINAL_ICON_WIDTH = 19;
    private static final int ORDINAL_ICON_HEIGHT = 13;

    private static final int CARDINAL_TEXTURE_WIDTH = 13;
    private static final int CARDINAL_TEXTURE_HEIGHT = 52;
    private static final int CARDINAL_ICON_WIDTH = 13;
    private static final int CARDINAL_ICON_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public DirectionHUD() {
        super(DIRECTION_SETTINGS.base);
    }

    @Override
    public String getName() {
        return "Direction HUD";
    }

    @Override
    public String getId() {
        return HUDId.DIRECTION.toString();
    }

    private String yawStr;
    private int iconIndex;
    private int width;
    private int height;
    private int color;
    private boolean includeOrdinal;

    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        float yaw = Math.round(MathHelper.wrapDegrees(CLIENT.cameraEntity.getYaw()) * 10.0F) / 10.0F;
        yawStr = Float.toString(yaw);
        int yawWidth = CLIENT.textRenderer.getWidth(yawStr) - 1;

        includeOrdinal = DIRECTION_SETTINGS.includeOrdinal;
        displayMode = getSettings().getDisplayMode();

        if (includeOrdinal) {
            iconIndex = getOrdinalDirectionIcon(yaw);

            width = displayMode.calculateWidth(ORDINAL_ICON_WIDTH, yawWidth);
            height = ORDINAL_ICON_HEIGHT;

            color = getDirectionColor(iconIndex) | 0xFF000000;

        } else {
            iconIndex = getCardinalDirectionIcon(yaw);

            width = displayMode.calculateWidth(CARDINAL_ICON_WIDTH, yawWidth);
            height = CARDINAL_ICON_HEIGHT;

            color = getDirectionColor(iconIndex * 2) | 0xFF000000;
        }

        setWidthHeightColor(width, height, color);

        return true;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {

        int w = getWidth();
        int h = getHeight();

        if (includeOrdinal) {
            RenderUtils.drawSmallHUD(
                    context,
                    yawStr,
                    x, y,
                    w, h,
                    DIRECTION_ORDINAL_TEXTURE,
                    0.0F, ORDINAL_ICON_HEIGHT * iconIndex,
                    ORDINAL_TEXTURE_WIDTH, ORDINAL_TEXTURE_HEIGHT,
                    ORDINAL_ICON_WIDTH, ORDINAL_ICON_HEIGHT,
                    color,
                    displayMode,
                    drawBackground
            );

        } else {
            RenderUtils.drawSmallHUD(
                    context,
                    yawStr,
                    x, y,
                    w, h,
                    DIRECTION_CARDINAL_TEXTURE,
                    0.0F, CARDINAL_ICON_HEIGHT * iconIndex,
                    CARDINAL_TEXTURE_WIDTH, CARDINAL_TEXTURE_HEIGHT,
                    CARDINAL_ICON_WIDTH, CARDINAL_ICON_HEIGHT,
                    color,
                    displayMode,
                    drawBackground
            );
        }
        return true;
    }

    private static int getOrdinalDirectionIcon(float yaw) {
        if (-22.5 <= yaw && yaw < 22.5) return 0;   //south
        else if (22.5 <= yaw && yaw < 67.5) return 1;   //southwest
        else if (67.5 <= yaw && yaw < 112.5) return 2;   //west
        else if (112.5 <= yaw && yaw < 157.5) return 3;   //northwest
        else if (157.5 <= yaw || yaw < -157.5) return 4;   //north
        else if (-157.5 <= yaw && yaw < -112.5) return 5;   //northeast
        else if (-112.5 <= yaw && yaw < -67.5) return 6;   //east
        else if (-67.5 <= yaw && yaw < -22.5) return 7;   //southeast
        else return 0;
    }

    private static int getCardinalDirectionIcon(float yaw) {
        if (-45.0 <= yaw && yaw < 45.0) return 0;   //south
        else if (45.0 <= yaw && yaw < 135.0) return 1;   //west
        else if (135.0 <= yaw || yaw < -135.0) return 2;   //north
        else if (-135.0 <= yaw && yaw < -45.0) return 3;   //east
        else return 0;
    }

    private static int getDirectionColor(int icon) {
        return switch (icon) {
            case 0 -> DIRECTION_SETTINGS.directionColor.s;
            case 1 -> DIRECTION_SETTINGS.directionColor.sw;
            case 2 -> DIRECTION_SETTINGS.directionColor.w;
            case 3 -> DIRECTION_SETTINGS.directionColor.nw;
            case 4 -> DIRECTION_SETTINGS.directionColor.n;
            case 5 -> DIRECTION_SETTINGS.directionColor.ne;
            case 6 -> DIRECTION_SETTINGS.directionColor.e;
            case 7 -> DIRECTION_SETTINGS.directionColor.se;
            default -> 0xFFFFFF;
        };
    }
}
