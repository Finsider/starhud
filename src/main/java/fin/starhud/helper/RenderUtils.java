package fin.starhud.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import fin.starhud.Helper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class RenderUtils {

    private static final Identifier DURABILITY_TEXTURE = Identifier.of("starhud", "hud/durability_bar.png");
    private static final Identifier DURABILITY_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/durability_background.png");

    // 10 bars + 9 gaps.
    private static final int DURABILITY_TEXTURE_WIDTH = (3 * 10) + 9;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static void fillRoundedRightSide(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2 - 1, y2, color);
        context.fill(x2 - 1, y1 + 1, x2, y2 - 1, color);
    }

    // get the durability "steps" or progress.
    public static int getItemBarStep(ItemStack stack, int maxStep) {
        return MathHelper.clamp(Math.round(maxStep - (float) stack.getDamage() * maxStep / (float) stack.getMaxDamage()), 0, maxStep);
    }

    // color transition from pastel (red to green).
    public static int getItemBarColor(int stackStep, int maxStep) {
        return MathHelper.hsvToRgb(0.35F * stackStep / (float) maxStep, 0.45F, 0.95F);
    }

    public static void renderDurabilityHUD(DrawContext context, Identifier ICON, ItemStack stack, int x, int y, float v, int textureWidth, int textureHeight, int color, boolean drawBar, GrowthDirectionX textureGrowth) {
        if (drawBar) {
            renderDurabilityBar(context, ICON, stack, x, y, v, textureWidth, textureHeight, color, textureGrowth);
        } else {
            renderDurabilityNumber(context, ICON, stack, x, y, v, textureWidth, textureHeight, color, textureGrowth);
        }
    }

    public static void renderDurabilityBar(DrawContext context, Identifier ICON, ItemStack stack, int x, int y, float v, int textureWidth, int textureHeight, int color, GrowthDirectionX textureGrowth) {
        int step = getItemBarStep(stack, 10);
        int durabilityColor = getItemBarColor(step, 10) | 0xFF000000;

        x -= textureGrowth.getGrowthDirection(DURABILITY_TEXTURE_WIDTH);

        // draw the icon
        RenderUtils.drawTextureHUD(context, ICON, x, y, 0.0F, v, 13, 13, textureWidth, textureHeight, color);

        // draw the durability background and steps
        RenderUtils.drawTextureHUD(context, DURABILITY_BACKGROUND_TEXTURE, x + 14, y, 0.0F, 0.0F, 49, 13, 49, 13);
        if (step != 0) RenderUtils.drawTextureHUD(context, DURABILITY_TEXTURE, x + 19, y + 3, 0, 0, 4 * step, 7, 40, 7, durabilityColor);
    }

    // example render: ¹²³⁴/₅₆₇₈
    public static void renderDurabilityNumber(DrawContext context, Identifier ICON, ItemStack stack, int x, int y, float v, int textureWidth, int textureHeight, int color, GrowthDirectionX textureGrowth) {
        int damage = stack.getDamage();
        int maxDamage = stack.getMaxDamage();
        int remaining = maxDamage - damage;

        String remainingStr = Helper.toSuperscript(Integer.toString(remaining)) + '/';
        String maxDamageStr = Helper.toSubscript(Integer.toString(maxDamage));

        int remainingTextWidth = CLIENT.textRenderer.getWidth(remainingStr);
        int maxDamageTextWidth = CLIENT.textRenderer.getWidth(maxDamageStr);

        int textColor = getItemBarColor(remaining, maxDamage) | 0xFF000000;

        x -= textureGrowth.getGrowthDirection(remainingTextWidth + maxDamageTextWidth);

        RenderUtils.drawTextureHUD(context, ICON, x, y, 0.0F, v, 13, 13, textureWidth, textureHeight, color);
        fillRoundedRightSide(context, x + 14,  y, x + 14 + remainingTextWidth + maxDamageTextWidth + 10, y + 13, 0x80000000);

        // this is gore of my comfort character, call drawText twice except the second one has a -1 pixel offset.
        // Minecraft default subscript's font is 1 pixel to deep for my liking, so I have to shift them up.
        RenderUtils.drawTextHUD(context, remainingStr, x + 14 + 5, y + 3, textColor, false);
        RenderUtils.drawTextHUD(context, maxDamageStr, x + 14 + 5 + remainingTextWidth, y + 3 - 1, textColor, false);
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
        drawTextureAlphaColor(context, identifier, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }

    public static void drawTextureHUD(DrawContext context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        drawTextureAlpha(context, identifier, x, y, u, v, width, height, textureWidth, textureHeight);
    }

    public static void drawTextHUD(DrawContext context, String str, int x, int y, int color, boolean shadow) {
        context.drawText(CLIENT.textRenderer, str, x , y, color, shadow);
    }
}
