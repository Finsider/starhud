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

    private static Settings.DirectionSettings direction = Main.settings.directionSettings;

    private static final Identifier DIRECTION_TEXTURE = Identifier.of("starhud", "hud/direction.png");

    public static void renderDirectionHUD(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();

        Entity playerCamera = mc.cameraEntity;

        float yaw = Math.round(MathHelper.wrapDegrees(playerCamera.getYaw()) * 10.0F) / 10.0F;

        int icon = getDirectionIcon(yaw);
        int color = getDirectionColor(icon) | 0xFF000000;

        int width = 61;
        int height = 13;

        int x = Helper.defaultHUDLocationX(direction.originX, context, width) + direction.x;
        int y = Helper.defaultHUDLocationY(direction.originY, context, height) + direction.y;

        context.drawTexture(RenderLayer::getGuiTextured, DIRECTION_TEXTURE, x, y, 0.0F, icon * 13, width, height, width, height * 8, color);
        context.drawText(mc.textRenderer, Float.toString(yaw), x + 25, y + 3, color, false);
    }

    private static int getDirectionIcon(float yaw) {
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
