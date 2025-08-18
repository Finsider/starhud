package fin.starhud.helper;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class RenderUtils {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final GeneralSettings.HUDSettings HUD_SETTINGS = Main.settings.generalSettings.hudSettings;

    private static final int ITEM_HUD_ICON_WIDTH = 22;
    private static final int ITEM_HUD_ICON_HEIGHT = 22;

    public static boolean drawSmallHUD(String infoStr, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, int iconColor, HUDDisplayMode displayMode, boolean drawBackground, float scale) {
        if (infoStr == null) return false;

        OrderedText orderedText = OrderedText.styledForwardsVisitedString(infoStr, Style.EMPTY);
        return drawSmallHUD(orderedText, x, y, width, height, iconTexture, u, v, textureWidth, textureHeight, iconWidth, iconHeight, color, iconColor, displayMode, drawBackground, scale);
    }

    public static boolean drawSmallHUD(OrderedText infoText, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, int iconColor, HUDDisplayMode displayMode, boolean drawBackground, float scale) {
        if (infoText == null || iconTexture == null || displayMode == null)
            return false;

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (displayMode) {
            case ICON ->  {
                if (drawBackground)
                    fillRounded(x, y, x + iconWidth, y + iconHeight, 0x80000000, scale);
                drawTexture(iconTexture, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor, scale);
            }
            case INFO ->  {
                if (drawBackground)
                    fillRounded(x, y, x + width, y + height, 0x80000000, scale);
                drawText(infoText, x + padding, y + 3, color, false, scale);
            }
            case BOTH ->  {
                if (drawBackground) {
                    if (gap <= 0)
                        fillRounded(x, y, x + width, y + height, 0x80000000, scale);
                    else {
                        fillRoundedLeftSide(x, y, x + iconWidth, y + height, 0x80000000, scale);
                        fillRoundedRightSide(x + iconWidth + gap, y, x + width, y + height, 0x80000000, scale);
                    }
                }
                drawTexture(iconTexture, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor, scale);
                drawText(infoText, x + iconWidth + gap + padding, y + 3, color, false, scale);
            }
        }

        return true;
    }

    public static boolean drawSmallHUD(String infoStr, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, HUDDisplayMode displayMode, boolean drawBackground, float scale) {
        if (infoStr == null) return false;

        OrderedText orderedText = OrderedText.styledForwardsVisitedString(infoStr, Style.EMPTY);
        return drawSmallHUD(orderedText, x, y, width, height, iconTexture, u, v, textureWidth, textureHeight, iconWidth, iconHeight, color, color, displayMode, drawBackground, scale);
    }

    public static boolean drawSmallHUD(OrderedText infoText, int x, int y, int width, int height, Identifier iconTexture, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, int color, HUDDisplayMode displayMode, boolean drawBackground, float scale) {
        return drawSmallHUD(infoText, x, y, width, height, iconTexture, u, v, textureWidth, textureHeight, iconWidth, iconHeight, color, color, displayMode, drawBackground, scale);
    }

    public static boolean drawItemHUD(String str, int x, int y, int width, int height, ItemStack itemAsIcon, int textColor, HUDDisplayMode displayMode, boolean drawBackground, float scale) {
        if (str == null || itemAsIcon == null || displayMode == null)
            return false;

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (displayMode) {
            case ICON -> {
                if (drawBackground)
                    fillRounded(x, y, x + ITEM_HUD_ICON_WIDTH, y + ITEM_HUD_ICON_HEIGHT, 0x80000000, scale);
                drawItem(itemAsIcon, x + 3, y + 3, scale);
            }
            case INFO -> {
                if (drawBackground)
                    fillRounded(x, y, x + width, y + height, 0x80000000, scale);
                drawText(str, x + padding, y + 7, textColor, false, scale);
            }
            case BOTH -> {
                if (drawBackground) {
                    if (gap <= 0)
                        fillRounded(x, y, x + width, y + height, 0x80000000, scale);
                    else {
                        fillRoundedLeftSide(x, y, x + ITEM_HUD_ICON_WIDTH, y + height, 0x80000000, scale);
                        fillRoundedRightSide(x + ITEM_HUD_ICON_WIDTH + gap, y, x + width, y + height, 0x80000000, scale);
                    }
                }

                drawItem(itemAsIcon, x + 3, y + 3, scale);
                drawText(str, x + ITEM_HUD_ICON_WIDTH + gap + padding, y + 7, textColor, false, scale);
            }
        }

        return true;
    }

    public static void fillRoundedRightSide(int x1, int y1, int x2, int y2, int color, float scale) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            fill(x1, y1, x2 - 1, y2, color, scale);
            fill(x2 - 1, y1 + 1, x2, y2 - 1, color, scale);
        } else {
            fill(x1, y1, x2, y2, color, scale);
        }
    }

    public static void fillRoundedLeftSide(int x1, int y1, int x2, int y2, int color, float scale) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            fill(x1, y1 + 1, x1 + 1, y2 - 1, color, scale);
            fill(x1 + 1, y1, x2, y2, color, scale);
        } else {
            fill(x1, y1, x2, y2, color, scale);
        }
    }

    public static void fillRoundedUpperSide(int x1, int y1, int x2, int y2, int color, float scale) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            fill(x1 + 1, y1, x2 - 1, y1 + 1, color, scale);
            fill(x1, y1 + 1, x2, y2, color, scale);
        } else {
            fill(x1, y1, x2, y2, color, scale);
        }
    }

    public static void fillRoundedBottomSide(int x1, int y1, int x2, int y2, int color, float scale) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            fill(x1, y1, x2, y2 - 1, color, scale);
            fill(x1 + 1, y2 - 1, x2 - 1, y2, color, scale);
        } else {
            fill(x1, y1, x2, y2, color ,scale);
        }
    }

    public static void fillRounded(int x1, int y1, int x2, int y2, int color, float scale) {
        if (HUD_SETTINGS.drawBackgroundRounded) {
            fill(x1, y1 + 1, x1 + 1, y2 - 1, color, scale);
            fill(x1 + 1, y1, x2 - 1, y2, color, scale);
            fill(x2 - 1, y1 + 1, x2, y2 - 1, color, scale);
        } else {
            fill(x1, y1, x2, y2, color ,scale);
        }
    }

    public static void drawBorderedFill(int x1, int y1, int x2, int y2, int fillColor, int borderColor, float scale) {
        fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, fillColor, scale);
        drawBorder(x1, y1, x2 - x1, y2 - y1, borderColor, scale);
    }

    public static void drawBorder(int x, int y, int width, int height, int color, float scale) {
        fill(x, y, x + width, y + 1, color, scale);
        fill(x, y + height - 1, x + width, y + height, color, scale);
        fill(x, y + 1, x + 1, y + height - 1, color, scale);
        fill(x + width - 1, y + 1, x + width, y + height - 1, color, scale);
    }

    public static void drawText(String str, int x, int y, int color, boolean shadow, float scale) {
        if (str != null) {
            OrderedText orderedText = OrderedText.styledForwardsVisitedString(str, Style.EMPTY);
            drawText(orderedText, x, y, color, shadow, scale);
        }
    }

    public static void drawTexture(Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, float scale) {
        drawTexture(identifier, x, y, u, v, width, height, textureWidth, textureHeight, 0xFFFFFFFF, scale);
    }

    // batching.

    public record Fill(int x1, int y1, int x2, int y2, int color) {}
    public record DrawTexture(Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {}
    public record DrawText(OrderedText text, int x, int y, int color, boolean shadow) {}
    public record DrawItem(ItemStack stack, int x, int y) {}
    public record DrawStackOverlay(ItemStack stack, int x, int y) {}

    private static final Map<Float, List<Fill>> fills = new HashMap<>();
    private static final Map<Float, List<DrawTexture>> drawTextures = new HashMap<>();
    private static final Map<Float, List<DrawText>> drawTexts = new HashMap<>();
    private static final Map<Float, List<DrawItem>> drawItems = new HashMap<>();
    private static final Map<Float, List<DrawStackOverlay>> drawStackOverlays = new HashMap<>();

    public static void fill(int x1, int y1, int x2, int y2, int color, float scale) {
        fills.computeIfAbsent(scale, s -> new ArrayList<>())
                .add(new Fill(x1, y1, x2, y2, color));
    }

    public static void drawTexture(Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color, float scale) {
        drawTextures.computeIfAbsent(scale, s -> new ArrayList<>())
                .add(new DrawTexture(identifier, x, y, u, v, width, height, textureWidth, textureHeight, color));
    }

    public static void drawText(OrderedText text, int x, int y, int color, boolean shadow, float scale) {
        drawTexts.computeIfAbsent(scale, s -> new ArrayList<>())
                .add(new DrawText(text, x, y + HUD_SETTINGS.textYOffset, color, shadow));
    }

    public static void drawItem(ItemStack item, int x, int y, float scale) {
        drawItems.computeIfAbsent(scale, s -> new ArrayList<>())
                .add(new DrawItem(item, x, y));
    }

    public static void drawStackOverlay(ItemStack item, int x, int y, float scale) {
        drawStackOverlays.computeIfAbsent(scale, s -> new ArrayList<>())
                .add(new DrawStackOverlay(item, x, y));
    }

    public static void drawAll(DrawContext context) {
        float guiScale = CLIENT.getWindow().getScaleFactor();

        drawByType(context, fills, (ctx, f) ->
                ctx.fill(f.x1, f.y1, f.x2, f.y2, f.color),
                guiScale
        );

        drawByType(context, drawTextures, (ctx, t) ->
                ctx.drawTexture(RenderPipelines.GUI_TEXTURED, t.identifier, t.x, t.y, t.u, t.v, t.width, t.height, t.textureWidth, t.textureHeight, t.color),
                guiScale
        );

        drawByType(context, drawItems, (ctx, i) ->
                ctx.drawItem(i.stack, i.x, i.y),
                guiScale
        );

        drawByType(context, drawTexts, (ctx, t) ->
                ctx.drawText(CLIENT.textRenderer, t.text, t.x, t.y, t.color, t.shadow),
                guiScale
        );

        drawByType(context, drawStackOverlays, (ctx, o) ->
                ctx.drawStackOverlay(CLIENT.textRenderer, o.stack, o.x, o.y),
                guiScale
        );
    }

    private static <T> void drawByType(DrawContext context, Map<Float, List<T>> map, BiConsumer<DrawContext, T> drawFn, float guiScale) {
        for (Map.Entry<Float, List<T>> entry : map.entrySet()) {
            float scale = entry.getKey();
            List<T> list = entry.getValue();
            if (list == null || list.isEmpty()) continue;

            boolean needsTransform = scale != 0 && scale != guiScale;
            if (needsTransform) {
                context.getMatrices().pushMatrix();
                float scaleFactor = scale / guiScale;
                context.getMatrices().scale(scaleFactor, scaleFactor);
            }

            for (T element : list) {
                drawFn.accept(context, element);
            }

            if (needsTransform) {
                context.getMatrices().popMatrix();
            }
        }
    }

    public static void clearAll() {
        fills.clear();
        drawTextures.clear();
        drawTexts.clear();
        drawItems.clear();
        drawStackOverlays.clear();
    }
}
