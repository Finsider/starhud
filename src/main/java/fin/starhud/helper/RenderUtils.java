package fin.starhud.helper;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

public class RenderUtils {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final GeneralSettings.HUDSettings HUD_SETTINGS = Main.settings.generalSettings.hudSettings;

    private static final int ITEM_HUD_ICON_WIDTH = 22;
    private static final int ITEM_HUD_ICON_HEIGHT = 22;

    public static boolean drawSmallHUD(GuiGraphicsExtractor context, String infoStr, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, int iconColor, HUDDisplayMode displayMode, boolean drawBackground, boolean drawTextShadow) {
        if (iconTexture == null || infoStr == null || displayMode == null) return false;

        FormattedCharSequence orderedText = FormattedCharSequence.forward(infoStr, Style.EMPTY);
        return drawSmallHUD(context, orderedText, x, y, width, height, iconTexture, u, v, textureWidth, textureHeight, iconWidth, iconHeight, color, iconColor, displayMode, drawBackground, drawTextShadow);
    }

    public static boolean drawSmallHUD(GuiGraphicsExtractor context, FormattedCharSequence infoText, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, int iconColor, HUDDisplayMode displayMode, boolean drawBackground, boolean drawTextShadow) {
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
                drawTextHUD(context, infoText, x + padding, y + 3, color, drawTextShadow);
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
                drawTextHUD(context, infoText, x + iconWidth + gap + padding, y + 3, color, drawTextShadow);
            }
        }

        return true;
    }

    public static boolean drawSmallHUD(GuiGraphicsExtractor context, String infoStr, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, HUDDisplayMode displayMode, boolean drawBackground, boolean drawTextShadow) {
        if (infoStr == null || iconTexture == null || displayMode == null) return false;

        FormattedCharSequence orderedText = FormattedCharSequence.forward(infoStr, Style.EMPTY);
        return drawSmallHUD(context, orderedText, x, y, width, height, iconTexture, u, v, textureWidth, textureHeight, iconWidth, iconHeight, color, color, displayMode, drawBackground, drawTextShadow);
    }

    public static boolean drawSmallHUD(GuiGraphicsExtractor context, FormattedCharSequence infoText, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, HUDDisplayMode displayMode, boolean drawBackground, boolean drawTextShadow) {
        if (infoText == null || iconTexture == null || displayMode == null) return false;

        return drawSmallHUD(context, infoText, x, y, width, height, iconTexture, u, v, textureWidth, textureHeight, iconWidth, iconHeight, color, color, displayMode, drawBackground, drawTextShadow);
    }

    public static boolean drawItemHUD(GuiGraphicsExtractor context, FormattedCharSequence str, int x, int y, int width, int height, ItemStack itemAsIcon, int textColor, HUDDisplayMode displayMode, boolean drawBackground, boolean drawTextShadow) {
        if (str == null || itemAsIcon == null || displayMode == null) return false;

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (displayMode) {
            case ICON -> {
                if (drawBackground)
                    fillRounded(context, x, y, x + ITEM_HUD_ICON_WIDTH, y + ITEM_HUD_ICON_HEIGHT, 0x80000000);
                context.item(itemAsIcon, x + 3, y + 3);
            }
            case INFO -> {
                if (drawBackground)
                    fillRounded(context, x, y, x + width, y + height, 0x80000000);
                drawTextHUD(context, str, x + padding, y + 7, textColor, drawTextShadow);
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

                context.item(itemAsIcon, x + 3, y + 3);
                drawTextHUD(context, str, x + ITEM_HUD_ICON_WIDTH + gap + padding, y + 7, textColor, drawTextShadow);
            }
        }

        return true;
    }

    public static boolean drawItemHUD(GuiGraphicsExtractor context, String str, int x, int y, int width, int height, ItemStack itemAsIcon, int textColor, HUDDisplayMode displayMode, boolean drawBackground, boolean drawTextShadow) {
        if (str == null) return false;
        FormattedCharSequence orderedText = FormattedCharSequence.forward(str, Style.EMPTY);
        return drawItemHUD(context, orderedText, x, y, width, height, itemAsIcon, textColor, displayMode, drawBackground, drawTextShadow);
    }

    public static void fillRoundedRightSide(GuiGraphicsExtractor context, int x1, int y1, int x2, int y2, int color) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            context.fill(x1, y1, x2 - 1, y2, color);
            context.fill(x2 - 1, y1 + 1, x2, y2 - 1, color);
        } else {
            context.fill(x1, y1, x2, y2, color);
        }
    }

    public static void fillRoundedLeftSide(GuiGraphicsExtractor context, int x1, int y1, int x2, int y2, int color) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            context.fill(x1, y1 + 1, x1 + 1, y2 - 1, color);
            context.fill(x1 + 1, y1, x2, y2, color);
        } else {
            context.fill(x1, y1, x2, y2, color);
        }
    }

    public static void fillRounded(GuiGraphicsExtractor context, int x1, int y1, int x2, int y2, int color) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            context.fill(x1, y1 + 1, x1 + 1, y2 - 1, color);
            context.fill(x1 + 1, y1, x2 - 1, y2, color);
            context.fill(x2 - 1, y1 + 1, x2, y2 - 1, color);
        } else {
            context.fill(x1, y1, x2, y2, color);
        }
    }

    public static void drawBorder(GuiGraphicsExtractor context, int x, int y, int w, int h, int color) {
        context.fill(x, y, x + w, y + 1, color);
        context.fill(x, y, x + 1, y + h, color);
        context.fill(x, y + h - 1, x + w, y + h, color);
        context.fill(x + w - 1, y, x + w, y + h, color);
    }

    // for easier version porting.

    public static void drawTextureHUD(GuiGraphicsExtractor context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
        context.blit(RenderPipelines.GUI_TEXTURED, identifier, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }

    public static void drawTextureHUD(GuiGraphicsExtractor context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        context.blit(RenderPipelines.GUI_TEXTURED, identifier, x, y, u, v, width, height, textureWidth, textureHeight);
    }

    public static void drawTextHUD(GuiGraphicsExtractor context, String str, int x, int y, int color, boolean shadow) {
        if (str != null) {
            FormattedCharSequence orderedText = FormattedCharSequence.forward(str, Style.EMPTY);
            drawTextHUD(context, orderedText, x, y, color, shadow);
        }
    }

    public static void drawTextHUD(GuiGraphicsExtractor context, FormattedCharSequence text, int x, int y, int color, boolean shadow) {
        context.text(CLIENT.font, text, x, y + HUD_SETTINGS.textYOffset, color, shadow);
    }
}
