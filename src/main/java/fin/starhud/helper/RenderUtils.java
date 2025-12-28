package fin.starhud.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

public class RenderUtils {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final GeneralSettings.HUDSettings HUD_SETTINGS = Main.settings.generalSettings.hudSettings;

    private static final int ITEM_HUD_ICON_WIDTH = 22;
    private static final int ITEM_HUD_ICON_HEIGHT = 22;

    public static boolean drawSmallHUD(DrawContext context, String infoStr, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, int iconColor, HUDDisplayMode displayMode, boolean drawBackground) {
        if (iconTexture == null || infoStr == null || displayMode == null) return false;

        OrderedText orderedText = OrderedText.styledForwardsVisitedString(infoStr, Style.EMPTY);
        return drawSmallHUD(context, orderedText, x, y, width, height, iconTexture, u, v, textureWidth, textureHeight, iconWidth, iconHeight, color, iconColor, displayMode, drawBackground);
    }

    public static boolean drawSmallHUD(DrawContext context, OrderedText infoText, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, int iconColor, HUDDisplayMode displayMode, boolean drawBackground) {
        if (infoText == null || iconTexture == null || displayMode == null) return false;

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (displayMode) {
            case ICON ->  {
                if (drawBackground)
                    fillRounded(context, x, y, x + iconWidth, y + iconHeight, 0x80000000);
                drawTextureHUD(context, iconTexture, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor);
            }
            case INFO ->  {
                if (drawBackground)
                    fillRounded(context, x, y, x + width, y + height, 0x80000000);
                drawTextHUD(context, infoText, x + padding, y + 3, color, false);
            }
            case BOTH ->  {
                if (drawBackground) {
                    if (gap <= 0)
                        fillRounded(context, x, y, x + width, y + height, 0x80000000);
                    else {
                        fillRoundedLeftSide(context, x, y, x + iconWidth, y + height, 0x80000000);
                        fillRoundedRightSide(context, x + iconWidth + gap, y, x + width, y + height, 0x80000000);
                    }
                }
                drawTextureHUD(context, iconTexture, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor);
                drawTextHUD(context, infoText, x + iconWidth + gap + padding, y + 3, color, false);
            }
        }

        return true;
    }

    public static boolean drawSmallHUD(DrawContext context, String infoStr, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, HUDDisplayMode displayMode, boolean drawBackground) {
        if (infoStr == null || iconTexture == null || displayMode == null) return false;

        OrderedText orderedText = OrderedText.styledForwardsVisitedString(infoStr, Style.EMPTY);
        return drawSmallHUD(context, orderedText, x, y, width, height, iconTexture, u, v, textureWidth, textureHeight, iconWidth, iconHeight, color, color, displayMode, drawBackground);
    }

    public static boolean drawSmallHUD(DrawContext context, OrderedText infoText, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, HUDDisplayMode displayMode, boolean drawBackground) {
        if (infoText == null || iconTexture == null || displayMode == null) return false;

        return drawSmallHUD(context, infoText, x, y, width, height, iconTexture, u, v, textureWidth, textureHeight, iconWidth, iconHeight, color, color, displayMode, drawBackground);
    }

    public static boolean drawItemHUD(DrawContext context, String str, int x, int y, int width, int height, ItemStack itemAsIcon, int textColor, HUDDisplayMode displayMode, boolean drawBackground) {
        if (str == null || itemAsIcon == null || displayMode == null) return false;

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (displayMode) {
            case ICON -> {
                if (drawBackground)
                    fillRounded(context, x, y, x + ITEM_HUD_ICON_WIDTH, y + ITEM_HUD_ICON_HEIGHT, 0x80000000);
                context.drawItem(itemAsIcon, x + 3, y + 3);
            }
            case INFO -> {
                if (drawBackground)
                    fillRounded(context, x, y, x + width, y + height, 0x80000000);
                drawTextHUD(context, str, x + padding, y + 7, textColor, false);
            }
            case BOTH -> {
                if (drawBackground) {
                    if (gap <= 0)
                        fillRounded(context, x, y, x + width, y + height, 0x80000000);
                    else {
                        fillRoundedLeftSide(context, x, y, x + ITEM_HUD_ICON_WIDTH, y + height, 0x80000000);
                        fillRoundedRightSide(context, x + ITEM_HUD_ICON_WIDTH + gap, y, x + width, y + height, 0x80000000);
                    }
                }

                context.drawItem(itemAsIcon, x + 3, y + 3);
                drawTextHUD(context, str, x + ITEM_HUD_ICON_WIDTH + gap + padding, y + 7, textColor, false);
            }
        }

        return true;
    }

    public static void fillRoundedRightSide(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            context.fill(x1, y1, x2 - 1, y2, color);
            context.fill(x2 - 1, y1 + 1, x2, y2 - 1, color);
        } else {
            context.fill(x1, y1, x2, y2, color);
        }
    }

    public static void fillRoundedLeftSide(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            context.fill(x1, y1 + 1, x1 + 1, y2 - 1, color);
            context.fill(x1 + 1, y1, x2, y2, color);
        } else {
            context.fill(x1, y1, x2, y2, color);
        }
    }

    public static void fillRounded(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            context.fill(x1, y1 + 1, x1 + 1, y2 - 1, color);
            context.fill(x1 + 1, y1, x2 - 1, y2, color);
            context.fill(x2 - 1, y1 + 1, x2, y2 - 1, color);
        } else {
            context.fill(x1, y1, x2, y2, color);
        }
    }

    public static void drawBorder(DrawContext context, int x, int y, int w, int h, int color) {
        context.fill(x, y, x + w, y + 1, color);
        context.fill(x, y, x + 1, y + h, color);
        context.fill(x, y + h - 1, x + w, y + h, color);
        context.fill(x + w - 1, y, x + w, y + h, color);
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

    // for easier version porting.

    public static void drawTextureHUD(DrawContext context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
        if (identifier == null)
            return;
        drawTextureAlphaColor(context, identifier, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }

    public static void drawTextureHUD(DrawContext context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        if (identifier == null)
            return;
        drawTextureAlpha(context, identifier, x, y, u, v, width, height, textureWidth, textureHeight);
    }

    public static void drawTextHUD(DrawContext context, String str, int x, int y, int color, boolean shadow) {
        if (str != null) {
            OrderedText orderedText = OrderedText.styledForwardsVisitedString(str, Style.EMPTY);
            drawTextHUD(context, orderedText, x, y, color, shadow);
        }
    }

    public static void drawTextHUD(DrawContext context, OrderedText text, int x, int y, int color, boolean shadow) {
        context.drawText(CLIENT.textRenderer, text, x, y + HUD_SETTINGS.textYOffset, color, shadow);
    }
}
