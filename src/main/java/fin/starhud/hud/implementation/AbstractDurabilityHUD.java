package fin.starhud.hud.implementation;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.BaseHUDSettings;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.hud.DurabilitySettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static fin.starhud.helper.HUDDisplayMode.ICON;
import static fin.starhud.helper.HUDDisplayMode.INFO;

public abstract class AbstractDurabilityHUD extends AbstractHUD {

    protected final DurabilitySettings durabilitySettings;

    private static final GeneralSettings.HUDSettings HUD_SETTINGS = Main.settings.generalSettings.hudSettings;

    private static final Identifier BIG_DURABILITY_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/big_durability_background.png");
    private static final Identifier BIG_DURABILITY_TEXTURE = Identifier.of("starhud", "hud/big_durability_bar.png");
    private static final int BIG_DURABILITY_TEXTURE_WIDTH = 70;
    private static final int BIG_DURABILITY_TEXTURE_HEIGHT = 14;

    private static final Identifier DURABILITY_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/durability_background.png");
    private static final Identifier DURABILITY_TEXTURE = Identifier.of("starhud", "hud/durability_bar.png");
    private static final int DURABILITY_TEXTURE_WIDTH = 40;
    private static final int DURABILITY_TEXTURE_HEIGHT = 7;

    private static final int ITEM_BACKGROUND_WIDTH = 3 + 16 + 3;
    private static final int ITEM_BACKGROUND_HEIGHT = 3 + 16 + 3;

    private static final int ICON_BACKGROUND_WIDTH = 13;
    private static final int ICON_BACKGROUND_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private ItemStack stack;

    private int stackDamage = 0;
    private int stackMaxDamage = 0;

    private String str = null;
    private String str2 = null;

    private int durabilityColor;
    private int iconColor;
    private int step;

    private int remainingTextWidth;

    private DurabilitySettings.DisplayMode displayMode = null;
    private boolean drawItem;

    private HUDDisplayMode hudDisplayMode = null;

    public AbstractDurabilityHUD(BaseHUDSettings baseHUDSettings, DurabilitySettings durabilitySettings) {
        super(baseHUDSettings);
        this.durabilitySettings = durabilitySettings;
    }

    public abstract ItemStack getStack();
    public abstract int getIconColor();

    @Override
    public boolean collectHUDInformation() {
        stack = getStack();

        if (stack.isEmpty() || !stack.isDamageable())
            return false;

        stackDamage = stack.getDamage();
        stackMaxDamage = stack.getMaxDamage();

        hudDisplayMode = getSettings().getDisplayMode();
        displayMode = durabilitySettings.displayMode;
        drawItem = durabilitySettings.drawItem;
        iconColor = getIconColor();

        int width = processWidth();
        int height = drawItem ? ITEM_BACKGROUND_HEIGHT : ICON_BACKGROUND_HEIGHT;

        durabilityColor = getItemBarColor(stackMaxDamage - stackDamage, stackMaxDamage) | 0xFF000000;

        setWidthHeightColor(width, height, iconColor);

        return true;
    }

    public int processWidth() {
        if (drawItem) {
            return processWidthItem();
        } else {
            return processWidthIcon();
        }
    }

    public int processWidthItem() {
        return switch (displayMode) {
            case BAR -> processWidthItemBar();
            case FRACTIONAL -> processWidthItemNumber();
            case VALUE_ONLY -> processWidthItemValue();
            case PERCENTAGE -> processWidthItemPercentage();
            case COMPACT -> processWidthItemCompact();
        };
    }

    public int processWidthItemBar() {
        step = getItemBarStep(stackDamage, stackMaxDamage, 10);
        durabilityColor = getItemBarColor(step, 10) | 0xFF000000;

        return hudDisplayMode.calculateWidth(ITEM_BACKGROUND_WIDTH, BIG_DURABILITY_TEXTURE_WIDTH - 1);
    }

    public int processWidthItemNumber() {
        int damage = stackDamage;
        int maxDamage = stackMaxDamage;
        int remaining = maxDamage - damage;

        str = remaining + "/" + maxDamage;

        step = getItemBarStep(stackDamage, stackMaxDamage, 10);

        int strWidth = CLIENT.textRenderer.getWidth(str);
        return hudDisplayMode.calculateWidth(ITEM_BACKGROUND_WIDTH, strWidth);
    }

    public int processWidthItemValue() {
        int remaining = stackMaxDamage - stackDamage;
        str = Integer.toString(remaining);
        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        return hudDisplayMode.calculateWidth(ITEM_BACKGROUND_WIDTH, strWidth);
    }

    public int processWidthItemPercentage() {
        int remaining = stackMaxDamage - stackDamage;
        int percentage = ((remaining * 100) / stackMaxDamage);

        str = percentage + "%";
        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        return hudDisplayMode.calculateWidth(ITEM_BACKGROUND_WIDTH, strWidth);
    }

    public int processWidthItemCompact() {
        return ITEM_BACKGROUND_WIDTH;
    }

    public int processWidthIcon() {
        return switch (displayMode) {
            case BAR -> processWidthIconBar();
            case FRACTIONAL -> processWidthIconNumber();
            case VALUE_ONLY -> processWidthIconValue();
            case PERCENTAGE -> processWidthIconPercentage();
            case COMPACT -> processWidthIconCompact();
        };
    }

    public int processWidthIconBar() {
        step = getItemBarStep(stackDamage, stackMaxDamage, 10);

        return hudDisplayMode.calculateWidth(ICON_BACKGROUND_WIDTH, DURABILITY_TEXTURE_WIDTH - 1);
    }

    public int processWidthIconNumber() {
        int damage = stackDamage;
        int maxDamage = stackMaxDamage;
        int remaining = maxDamage - damage;

        str = Helper.toSuperscript(Integer.toString(remaining)) + '/';
        str2 = Helper.toSubscript(Integer.toString(maxDamage));
        step = getItemBarStep(stackDamage, stackMaxDamage, 10);

        remainingTextWidth = CLIENT.textRenderer.getWidth(str);

        int totalTextWidth = remainingTextWidth + CLIENT.textRenderer.getWidth(str2) - 1;

        return hudDisplayMode.calculateWidth(ICON_BACKGROUND_WIDTH, totalTextWidth);
    }

    public int processWidthIconValue() {
        int remaining = stackMaxDamage - stackDamage;

        str = Integer.toString(remaining);

        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        return hudDisplayMode.calculateWidth(ICON_BACKGROUND_WIDTH, strWidth);
    }

    public int processWidthIconPercentage() {

        int remainingDamage = stackMaxDamage - stackDamage;
        int percentage = ((remainingDamage) * 100 / stackMaxDamage);

        str = percentage + "%";
        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        return hudDisplayMode.calculateWidth(ICON_BACKGROUND_WIDTH, strWidth);
    }

    public int processWidthIconCompact() {
        step = getItemBarStep(stackDamage, stackMaxDamage, 9);
        return ICON_BACKGROUND_WIDTH;
    }

    // get the durability "steps" or progress.
    public static int getItemBarStep(int stackDamage, int stackMaxDamage, int maxStep) {
        return MathHelper.clamp(Math.round(maxStep - (float) stackDamage * maxStep / (float) stackMaxDamage), 0, maxStep);
    }

    // color transition from pastel (red to green).
    public static int getItemBarColor(int stackStep, int maxStep) {
        return MathHelper.hsvToRgb(0.35F * stackStep / (float) maxStep, 0.45F, 0.95F);
    }

    public boolean renderDurabilityHUD(DrawContext context, Identifier iconTexture, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground) {
        if (drawItem) {
            return renderItemDurability(context, x , y, drawBackground);
        } else {
            return renderDurability(context, iconTexture, x, y, u, v, textureWidth, textureHeight, iconWidth, iconHeight, drawBackground);
        }
    }

    public boolean renderItemDurability(DrawContext context, int x, int y, boolean drawBackground) {
        if (displayMode == null) return false;
        return switch (displayMode) {
            case FRACTIONAL, VALUE_ONLY, PERCENTAGE -> RenderUtils.drawItemHUD(context, str, x, y, getWidth(), getHeight(), stack, durabilityColor, hudDisplayMode, drawBackground);
            case BAR -> renderItemDurabilityBar(context, x, y, drawBackground);
            case COMPACT -> renderItemDurabilityCompact(context, x, y, drawBackground);
        };
    }

    public boolean renderItemDurabilityBar(DrawContext context, int x, int y, boolean drawBackground) {
        if (stack == null) return false;

        int w = getWidth();
        int h = getHeight();

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (hudDisplayMode) {
            case ICON -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
                context.drawItem(stack, x + 3, y + 3);
            }
            case INFO -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
                RenderUtils.drawTextureHUD(context, BIG_DURABILITY_BACKGROUND_TEXTURE, x + padding, y + 4, 0.0F, 0.0F, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT);
                if (step != 0)
                    RenderUtils.drawTextureHUD(context, BIG_DURABILITY_TEXTURE, x + padding, y + 4, 0.0F, 0.0F, step * 7, BIG_DURABILITY_TEXTURE_HEIGHT, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, durabilityColor);
            }
            case BOTH -> {
                // draw background for the item texture
                if (drawBackground) {
                    if (gap <= 0) {
                        RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
                    } else {
                        RenderUtils.fillRoundedLeftSide(context, x, y, x + ITEM_BACKGROUND_WIDTH, y + h, 0x80000000);
                        RenderUtils.fillRoundedRightSide(context, x + ITEM_BACKGROUND_WIDTH + gap, y, x + w, y + h, 0x80000000);
                    }
                }
                // draw content
                context.drawItem(stack, x + 3, y + 3);

                RenderUtils.drawTextureHUD(context, BIG_DURABILITY_BACKGROUND_TEXTURE, x + ITEM_BACKGROUND_WIDTH + gap + padding, y + 4, 0.0F, 0.0F, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT);
                if (step != 0)
                    RenderUtils.drawTextureHUD(context, BIG_DURABILITY_TEXTURE, x + ITEM_BACKGROUND_WIDTH + gap + padding, y + 4, 0.0F, 0.0F, step * 7, BIG_DURABILITY_TEXTURE_HEIGHT, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, durabilityColor);
            }
        }

        return true;
    }

    public boolean renderItemDurabilityCompact(DrawContext context, int x, int y, boolean drawBackground) {
        if (stack == null || hudDisplayMode == null) return false;

        if (drawBackground)
            RenderUtils.fillRounded(context, x, y, x + getWidth(), y + getHeight(), 0x80000000);

        if (hudDisplayMode != INFO)
            context.drawItem(stack, x + 3, y + 3);

        if (hudDisplayMode != ICON)
            context.drawStackOverlay(CLIENT.textRenderer, stack, x + 3, y + 3);

        return true;
    }

    public boolean renderDurability(DrawContext context, Identifier ICON, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground) {
        if (displayMode == null) return false;
        return switch (displayMode) {
            case FRACTIONAL -> renderDurabilityNumber(context, ICON, x, y, u, v, textureWidth, textureHeight, iconWidth, iconHeight, drawBackground);
            case BAR -> renderDurabilityBar(context, ICON, x, y, u, v, textureWidth, textureHeight, iconWidth, iconHeight, drawBackground);
            case VALUE_ONLY, PERCENTAGE -> RenderUtils.drawSmallHUD(context, str, x, y, getWidth(), getHeight(), ICON, u, v, textureWidth, textureHeight, iconWidth, iconHeight, durabilityColor, iconColor, hudDisplayMode, drawBackground);
            case COMPACT -> renderDurabilityCompact(context, ICON, x, y, u, v, textureWidth, textureHeight, iconWidth, iconHeight, drawBackground);
        };
    }

    public boolean renderDurabilityBar(DrawContext context, Identifier ICON, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground) {
        if (ICON == null || hudDisplayMode == null)
            return false;

        int w = getWidth();
        int h = getHeight();

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (hudDisplayMode) {
            case ICON -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + iconWidth, y + iconHeight, 0x80000000);
                RenderUtils.drawTextureHUD(context, ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor);
            }
            case INFO -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
                RenderUtils.drawTextureHUD(context, DURABILITY_BACKGROUND_TEXTURE, x + padding, y + 3, 0.0F, 0.0F, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT);
                if (step != 0) RenderUtils.drawTextureHUD(context, DURABILITY_TEXTURE, x + padding, y + 3, 0.0F, 0.0F, 4 * step, DURABILITY_TEXTURE_HEIGHT, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, this.durabilityColor);
            }
            case BOTH -> { // WIP INFO_ONLY bruh
                // draw the background
                if (drawBackground) {
                    if (gap <= 0)
                        RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
                    else {
                        RenderUtils.fillRoundedLeftSide(context, x, y, x + iconWidth, y + h, 0x80000000);
                        RenderUtils.fillRoundedRightSide(context, x + iconWidth + gap, y, x + w, y + h, 0x80000000);
                    }
                }

                RenderUtils.drawTextureHUD(context, ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor);

                // draw the durability background and steps
                RenderUtils.drawTextureHUD(context, DURABILITY_BACKGROUND_TEXTURE, x + iconWidth + gap + padding, y + 3, 0.0F, 0.0F, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT);
                if (step != 0) RenderUtils.drawTextureHUD(context, DURABILITY_TEXTURE, x + iconWidth + gap + padding, y + 3, 0.0F, 0.0F, 4 * step, DURABILITY_TEXTURE_HEIGHT, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, this.durabilityColor);
            }
        }

        return true;
    }

    // example render: ¹²³⁴/₅₆₇₈
    public boolean renderDurabilityNumber(DrawContext context, Identifier ICON, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground) {
        if (ICON == null || str == null || str2 == null || hudDisplayMode == null) return false;
        int w = getWidth();
        int h = getHeight();

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (hudDisplayMode) {
            case ICON -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + iconWidth, y + iconHeight, 0x80000000);
                RenderUtils.drawTextureHUD(context, ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor);
            }
            case INFO -> {
                if (drawBackground)
                    RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
                RenderUtils.drawTextHUD(context, str, x + padding, y + 3, durabilityColor, false);
                RenderUtils.drawTextHUD(context, str2, x + padding + remainingTextWidth, y + 3 - 1, durabilityColor, false);
            }
            case BOTH ->  {

                if (drawBackground) {
                    if (gap <= 0)
                        RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
                    else {
                        RenderUtils.fillRoundedLeftSide(context, x, y, x + iconWidth, y + h, 0x80000000);
                        RenderUtils.fillRoundedRightSide(context, x + iconWidth + gap, y, x + w, y + h, 0x80000000);
                    }
                }
                RenderUtils.drawTextureHUD(context, ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor);

                RenderUtils.drawTextHUD(context, str, x + iconWidth + gap + padding, y + 3, durabilityColor, false);
                RenderUtils.drawTextHUD(context, str2, x + iconWidth + gap + padding + remainingTextWidth, y + 3 - 1, durabilityColor, false);
            }
        }

        return true;
    }

    public boolean renderDurabilityCompact(DrawContext context, Identifier ICON, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground) {

        if (ICON == null || hudDisplayMode == null) return false;

        if (drawBackground)
            RenderUtils.fillRounded(context, x, y, x + iconWidth, y + iconHeight, 0x80000000);

        if (hudDisplayMode != INFO) {
            RenderUtils.drawTextureHUD(context, ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor);
        }

        if (hudDisplayMode != HUDDisplayMode.ICON) {
            int durabilityWidth = 9;
            int firstX = x + ((iconWidth - durabilityWidth) / 2);
            int firstY = y + iconHeight - 2;

            context.fill(firstX, firstY, firstX + durabilityWidth, firstY + 1, 0x80000000);
            context.fill(firstX, firstY, firstX + step, firstY + 1, durabilityColor);
        }

        return true;
    }
}
