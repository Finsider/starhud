package fin.starhud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class Helper {

    public enum ScreenAlignmentX {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum ScreenAlignmentY {
        TOP,
        MIDDLE,
        BOTTOM,
    }

    public enum TextGrowthDirection {
        LEFT,
        CENTER,
        RIGHT,
    }

    public static int defaultHUDAlignmentX(ScreenAlignmentX alignmentX, int scaledWidth, int HUDWidth) {
        return switch (alignmentX) {
            case LEFT -> 0;
            case CENTER -> (scaledWidth - HUDWidth) / 2;
            case RIGHT -> scaledWidth - HUDWidth;
        };
    }

    public static int defaultHUDAlignmentY(ScreenAlignmentY alignmentY, int scaledHeight, int HUDHeight) {
        return switch (alignmentY) {
            case TOP -> 0;
            case MIDDLE -> (scaledHeight - HUDHeight) / 2;
            case BOTTOM -> scaledHeight - HUDHeight;
        };
    }

    public static int getTextGrowthDirection(TextGrowthDirection textGrowthDirection, int textWidth) {
        return switch (textGrowthDirection) {
            case LEFT -> textWidth;
            case CENTER -> textWidth / 2;
            case RIGHT -> 0;
        };
    }

    public static void fillRoundedRightSide(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2 - 1, y2, color);
        context.fill(x2 - 1, y1 + 1, x2, y2 - 1, color);
    }

    public static void drawTextureAlphaColor(DrawContext context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {

        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(red, green, blue, alpha);
        context.drawTexture(identifier, x, y, u, v, width, height, textureWidth, textureHeight);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    public static void drawTextureAlpha(DrawContext context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        RenderSystem.enableBlend();
        context.drawTexture(identifier, x, y, u, v, width, height, textureWidth, textureHeight);
        RenderSystem.disableBlend();
    }

    public static void drawTextureColor(DrawContext context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        RenderSystem.setShaderColor(red, green, blue, alpha);
        context.drawTexture(identifier, x, y, u, v, width, height, textureWidth, textureHeight);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
