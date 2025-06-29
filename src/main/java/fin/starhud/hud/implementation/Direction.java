package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.DirectionSettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class Direction extends AbstractHUD {

    private static final DirectionSettings DIRECTION_SETTINGS = Main.settings.directionSettings;

    private static final Identifier DIRECTION_CARDINAL_TEXTURE = Identifier.of("starhud", "hud/direction.png");
    private static final Identifier DIRECTION_ORDINAL_TEXTURE = Identifier.of("starhud", "hud/direction_ordinal.png");

    private static final int TEXTURE_HEIGHT = 13;

    private static final int TEXTURE_CARDINAL_WIDTH = 55;
    private static final int TEXTURE_ORDINAL_WIDTH = 61;

    private static final int CARDINAL_ICON_AMOUNT = 4;
    private static final int ORDINAL_ICON_AMOUNT = 8;

    private static final int CARDINAL_TEXT_OFFSET = 19;
    private static final int ORDINAL_TEXT_OFFSET = 25;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public Direction() {
        super(DIRECTION_SETTINGS.base);
    }

    @Override
    public void renderHUD(DrawContext context) {
        float yaw = Math.round(MathHelper.wrapDegrees(CLIENT.cameraEntity.getYaw()) * 10.0F) / 10.0F;

        if (DIRECTION_SETTINGS.includeOrdinal) {
            int icon = getOrdinalDirectionIcon(yaw);
            int color = getDirectionColor(icon) | 0xFF000000;

            RenderUtils.drawTextureHUD(context, DIRECTION_ORDINAL_TEXTURE, x, y, 0.0F, icon * 13, TEXTURE_ORDINAL_WIDTH, TEXTURE_HEIGHT, TEXTURE_ORDINAL_WIDTH, TEXTURE_HEIGHT * ORDINAL_ICON_AMOUNT, color);
            RenderUtils.drawTextHUD(context, Float.toString(yaw), x + ORDINAL_TEXT_OFFSET, y + 3, color, false);
        } else {
            int icon = getCardinalDirectionIcon(yaw);
            int color = getDirectionColor(icon * 2) | 0xFF000000;

            RenderUtils.drawTextureHUD(context, DIRECTION_CARDINAL_TEXTURE, x, y, 0.0F, icon * 13, TEXTURE_CARDINAL_WIDTH, TEXTURE_HEIGHT, TEXTURE_CARDINAL_WIDTH, TEXTURE_HEIGHT * CARDINAL_ICON_AMOUNT, color);
            RenderUtils.drawTextHUD(context, Float.toString(yaw), x + CARDINAL_TEXT_OFFSET, y + 3, color, false);
        }
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

    @Override
    public int getBaseHUDWidth() {
        return DIRECTION_SETTINGS.includeOrdinal ? TEXTURE_ORDINAL_WIDTH : TEXTURE_CARDINAL_WIDTH;
    }

    @Override
    public int getBaseHUDHeight() {
        return TEXTURE_HEIGHT;
    }
}
