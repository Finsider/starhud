package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class direction {

    private static Settings.DirectionSettings direction = Main.settings.directionSettings;

    private static final Identifier DIRECTION_TEXTURE = Identifier.of("starhud", "hud/direction.png");
    private static final Identifier DIRECTION_INCLUDE_ORDINAL_TEXTURE = Identifier.of("starhud", "hud/direction_ordinal.png");

    private static int width;
    private static final int height = 13;

    private static int icon;
    private static int color;
    private static int iconAmount;
    private static int textX;
    private static Identifier texture;

    private static boolean LAST_UPDATED_includeOrdinal = direction.includeOrdinal;

    public static void renderDirectionHUD(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        Entity playerCamera = client.cameraEntity;

        float yaw = Math.round(MathHelper.wrapDegrees(playerCamera.getYaw()) * 10.0F) / 10.0F;

        boolean includeOrdinal = direction.includeOrdinal;

        // silly way to cache all the variables
        if (LAST_UPDATED_includeOrdinal != includeOrdinal) {
            LAST_UPDATED_includeOrdinal = includeOrdinal;
            if (includeOrdinal) {
                texture = DIRECTION_INCLUDE_ORDINAL_TEXTURE;
                width = 61;
                iconAmount = 8;
                textX = 25;
            } else {
                texture = DIRECTION_TEXTURE;
                width = 55;
                iconAmount = 4;
                textX = 19;
            }
        }

        if (includeOrdinal) {
            icon = getOrdinalDirectionIcon(yaw);
            color = getDirectionColor(icon) | 0xFF000000;
        } else {
            icon = getCardinalDirectionIcon(yaw);
            color = getDirectionColor(icon * 2) | 0xFF000000;
        }

        int x = Helper.defaultHUDAlignmentX(direction.originX, context.getScaledWindowWidth(), width) + direction.x;
        int y = Helper.defaultHUDAlignmentY(direction.originY, context.getScaledWindowHeight(), height) + direction.y;

        Helper.drawTextureAlphaColor(context, texture, x, y, 0.0F, icon * 13, width, height, width, height * iconAmount, color);
        context.drawText(client.textRenderer, Float.toString(yaw), x + textX, y + 3, color, false);
    }

    private static int getOrdinalDirectionIcon(float yaw) {
        if (-22.5 <= yaw && yaw < 22.5)         return 0;   //south
        else if (22.5 <= yaw && yaw < 67.5)     return 1;   //southwest
        else if (67.5 <= yaw && yaw < 112.5)    return 2;   //west
        else if (112.5 <= yaw && yaw < 157.5)   return 3;   //northwest
        else if (157.5 <= yaw || yaw < -157.5)  return 4;   //north
        else if (-157.5 <= yaw && yaw < -112.5) return 5;   //northeast
        else if (-112.5 <= yaw && yaw < -67.5)  return 6;   //east
        else if (-67.5 <= yaw && yaw < -22.5)   return 7;   //southeast
        else return 0;
    }

    private static int getCardinalDirectionIcon(float yaw) {
        if (-45.0 <= yaw && yaw < 45.0)         return 0;   //south
        else if (45.0 <= yaw && yaw < 135.0)    return 1;   //west
        else if (135.0 <= yaw || yaw < -135.0)  return 2;   //north
        else if (-135.0 <= yaw && yaw < -45.0)  return 3;   //east
        else return 0;
    }

    private static int getDirectionColor(int icon) {
        return switch (icon) {
            case 0 -> direction.directionColor.s;
            case 1 -> direction.directionColor.sw;
            case 2 -> direction.directionColor.w;
            case 3 -> direction.directionColor.nw;
            case 4 -> direction.directionColor.n;
            case 5 -> direction.directionColor.ne;
            case 6 -> direction.directionColor.e;
            case 7 -> direction.directionColor.se;
            default -> 0xFFFFFF;
        };
    }
}