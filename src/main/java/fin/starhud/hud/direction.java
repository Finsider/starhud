package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class direction {

    private static final Settings.DirectionSettings direction = Main.settings.directionSettings;

    private static final Identifier DIRECTION_TEXTURE = Identifier.of("starhud", "hud/direction.png");
    private static final Identifier DIRECTION_INCLUDE_ORDINAL_TEXTURE = Identifier.of("starhud", "hud/direction_ordinal.png");

    private static final int height = 13;

    private static final int width_cardinal = 55;
    private static final int width_ordinal = 61;

    private static final int iconAmount_cardinal = 4;
    private static final int iconAmount_ordinal = 8;

    private static final int textX_cardinal = 19;
    private static final int textX_ordinal = 25;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void renderDirectionHUD(DrawContext context) {
        if ((direction.hideOn.f3 && Helper.isDebugHUDOpen()) || (direction.hideOn.chat && Helper.isChatFocused())) return;

        Entity playerCamera = client.cameraEntity;

        float yaw = Math.round(MathHelper.wrapDegrees(playerCamera.getYaw()) * 10.0F) / 10.0F;

        context.getMatrices().push();
        Helper.setHUDScale(context, direction.scale);

        if (direction.includeOrdinal) {
            int icon = getOrdinalDirectionIcon(yaw);
            int color = getDirectionColor(icon) | 0xFF000000;

            int x = Helper.calculatePositionX(direction.x, direction.originX, width_ordinal, direction.scale);
            int y = Helper.calculatePositionY(direction.y, direction.originY, height, direction.scale);

            context.drawTexture(RenderLayer::getGuiTextured, DIRECTION_INCLUDE_ORDINAL_TEXTURE, x, y, 0.0F, icon * 13, width_ordinal, height, width_ordinal, height * iconAmount_ordinal, color);
            context.drawText(client.textRenderer, Float.toString(yaw), x + textX_ordinal, y + 3, color, false);
        } else {
            int icon = getCardinalDirectionIcon(yaw);
            int color = getDirectionColor(icon * 2) | 0xFF000000;

            int x = Helper.calculatePositionX(direction.x, direction.originX, width_cardinal, direction.scale);
            int y = Helper.calculatePositionY(direction.y, direction.originY, height, direction.scale);

            context.drawTexture(RenderLayer::getGuiTextured, DIRECTION_TEXTURE, x, y, 0.0F, icon * 13, width_cardinal, height, width_cardinal, height * iconAmount_cardinal, color);
            context.drawText(client.textRenderer, Float.toString(yaw), x + textX_cardinal, y + 3, color, false);
        }

        context.getMatrices().pop();
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
