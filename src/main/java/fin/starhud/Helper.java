package fin.starhud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.Window;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;


public class Helper {

    private static final Identifier DURABILITY_TEXTURE = Identifier.of("starhud", "hud/durability.png");
    private static final Identifier DURABILITY_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/durability_background.png");
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Window window = client.getWindow();

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

    public enum GrowthDirection {
        LEFT,
        CENTER,
        RIGHT,
    }

    public static int defaultHUDAlignmentX(ScreenAlignmentX alignmentX, int scaledWidth) {
        return switch (alignmentX) {
            case LEFT -> 0;
            case CENTER -> scaledWidth / 2;
            case RIGHT -> scaledWidth;
        };
    }

    public static int defaultHUDAlignmentY(ScreenAlignmentY alignmentY, int scaledHeight) {
        return switch (alignmentY) {
            case TOP -> 0;
            case MIDDLE -> scaledHeight / 2;
            case BOTTOM -> scaledHeight;
        };
    }

    public static int calculateTextureOffsetX(ScreenAlignmentX alignmentX, int textureWidth) {
        return switch (alignmentX) {
            case LEFT -> 0;
            case CENTER -> textureWidth / 2;
            case RIGHT -> textureWidth;
        };
    }
    public static int calculateTextureOffsetY(ScreenAlignmentY alignmentY, int textureHeight) {
        return switch (alignmentY) {
            case TOP -> 0;
            case MIDDLE -> textureHeight / 2;
            case BOTTOM -> textureHeight;
        };
    }

    public static int getGrowthDirection(GrowthDirection growthDirection, int growableWidth) {
        return switch (growthDirection) {
            case LEFT -> growableWidth;
            case CENTER -> growableWidth / 2;
            case RIGHT -> 0;
        };
    }

    public static float scaledX(int scale) {
        return scale == 0 ? 1 : (float) window.getScaleFactor() / scale;
    }

    public static float scaledY(int scale) {
        return scale == 0 ? 1 : (float) window.getScaleFactor() / scale;
    }

    public static int calculatePositionX(int x, ScreenAlignmentX alignmentX, int HUDWidth, int HUDScale) {
        return x + (int) (defaultHUDAlignmentX(alignmentX, window.getScaledWidth()) * scaledX(HUDScale)) - calculateTextureOffsetX(alignmentX, HUDWidth);
    }

    public static int calculatePositionY(int y, ScreenAlignmentY alignmentY, int HUDHeight, int HUDScale) {
        return y + (int) (defaultHUDAlignmentY(alignmentY, window.getScaledHeight()) * scaledY(HUDScale)) - calculateTextureOffsetY(alignmentY, HUDHeight);
    }

    public static void setHUDScale(DrawContext context, int scale) {
        if (scale == 0) return;

        float scaleFactor = scale / (float) window.getScaleFactor();

        if (scaleFactor == 1) return;
        context.getMatrices().scale(scaleFactor, scaleFactor, scaleFactor);
    }

    public static void fillRoundedRightSide(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2 - 1, y2, color);
        context.fill(x2 - 1, y1 + 1, x2, y2 - 1, color);
    }

    // get the durability "steps" or progress.
    public static int getItemBarStep(ItemStack stack) {
        return MathHelper.clamp(Math.round(10 - (float)stack.getDamage() * 10 / (float)stack.getMaxDamage()), 0, 10);
    }

    // color transition from pastel (red to green).
    public static int getItemBarColor(int stackStep) {
        return MathHelper.hsvToRgb(0.35F * stackStep / 10.0F, 0.45F, 0.95F);
    }

    public static void renderItemDurabilityHUD(DrawContext context, Identifier ICON, ItemStack stack, int x, int y, float v, int textureWidth, int textureHeight, int color) {
        int step = getItemBarStep(stack);
        int color_dura = getItemBarColor(step) | 0xFF000000;

        // draw the icon
        context.drawTexture(RenderLayer::getGuiTextured, ICON, x, y, 0.0F, v, 13, 13, textureWidth, textureHeight, color);

        // draw the durability background and steps
        context.drawTexture(RenderLayer::getGuiTextured, DURABILITY_BACKGROUND_TEXTURE, x + 14, y, 0.0F, 0.0F, 49, 13, 49, 13);
        context.drawTexture(RenderLayer::getGuiTextured, DURABILITY_TEXTURE, x + 19, y + 3, 0, 0, 4 * step, 7, 40, 7, color_dura);
    }

    public static boolean isChatFocused() {
        return client.inGameHud.getChatHud().isChatFocused();
    }

    public static boolean isDebugHUDOpen() {
        return client.getDebugHud().shouldShowDebugHud();
    }
}