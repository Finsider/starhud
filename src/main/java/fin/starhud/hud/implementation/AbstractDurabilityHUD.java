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

    private int width = 0;
    private int height = 0;

    private String str;
    private String str2;

    private int durabilityColor;
    private int iconColor;
    private int step;

    private int remainingTextWidth;

    private DurabilitySettings.DisplayMode displayMode;
    private boolean drawItem;

    private HUDDisplayMode hudDisplayMode;

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

        width = processWidth();
        height = drawItem ? ITEM_BACKGROUND_HEIGHT : ICON_BACKGROUND_HEIGHT;

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

    public void drawDurability(Identifier iconTexture, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground, float scale) {
        if (drawItem) {
            drawItemDurability(x , y, drawBackground, scale);
        } else {
            drawIconDurability(iconTexture, x, y, u, v, textureWidth, textureHeight, iconWidth, iconHeight, drawBackground, scale);
        }
    }

    public void drawItemDurability(int x, int y, boolean drawBackground, float scale) {
        switch (displayMode) {
            case FRACTIONAL, VALUE_ONLY, PERCENTAGE -> RenderUtils.drawItemHUD(str, x, y, getWidth(), getHeight(), stack, durabilityColor, hudDisplayMode, drawBackground, scale);
            case BAR -> drawItemDurabilityBar(x, y, drawBackground, scale);
            case COMPACT -> drawItemDurabilityCompact(x, y, drawBackground, scale);
        }
    }

    public void drawItemDurabilityBar(int x, int y, boolean drawBackground, float scale) {
        int w = getWidth();
        int h = getHeight();

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (hudDisplayMode) {
            case ICON -> {
                if (drawBackground)
                    RenderUtils.fillRounded(x, y, x + w, y + h, 0x80000000, scale);
                RenderUtils.drawItem(stack, x + 3, y + 3, scale);
            }
            case INFO -> {
                if (drawBackground)
                    RenderUtils.fillRounded(x, y, x + w, y + h, 0x80000000, scale);
                RenderUtils.drawTexture(BIG_DURABILITY_BACKGROUND_TEXTURE, x + padding, y + 4, 0.0F, 0.0F, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, scale);
                if (step != 0)
                    RenderUtils.drawTexture(BIG_DURABILITY_TEXTURE, x + padding, y + 4, 0.0F, 0.0F, step * 7, BIG_DURABILITY_TEXTURE_HEIGHT, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, durabilityColor, scale);
            }
            case BOTH -> {
                // draw background for the item texture
                if (drawBackground) {
                    if (gap <= 0) {
                        RenderUtils.fillRounded(x, y, x + w, y + h, 0x80000000, scale);
                    } else {
                        RenderUtils.fillRoundedLeftSide(x, y, x + ITEM_BACKGROUND_WIDTH, y + h, 0x80000000, scale);
                        RenderUtils.fillRoundedRightSide(x + ITEM_BACKGROUND_WIDTH + gap, y, x + w, y + h, 0x80000000, scale);
                    }
                }
                // draw content
                RenderUtils.drawItem(stack, x + 3, y + 3, scale);

                RenderUtils.drawTexture(BIG_DURABILITY_BACKGROUND_TEXTURE, x + ITEM_BACKGROUND_WIDTH + gap + padding, y + 4, 0.0F, 0.0F, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, scale);
                if (step != 0)
                    RenderUtils.drawTexture(BIG_DURABILITY_TEXTURE, x + ITEM_BACKGROUND_WIDTH + gap + padding, y + 4, 0.0F, 0.0F, step * 7, BIG_DURABILITY_TEXTURE_HEIGHT, BIG_DURABILITY_TEXTURE_WIDTH, BIG_DURABILITY_TEXTURE_HEIGHT, durabilityColor, scale);
            }
        }
    }

    public void drawItemDurabilityCompact(int x, int y, boolean drawBackground, float scale) {
        if (drawBackground)
            RenderUtils.fillRounded(x, y, x + getWidth(), y + getHeight(), 0x80000000, scale);

        if (hudDisplayMode != INFO)
            RenderUtils.drawItem(stack, x + 3, y + 3, scale);

        if (hudDisplayMode != ICON)
            RenderUtils.drawStackOverlay(stack, x + 3, y + 3, scale);
    }

    public void drawIconDurability(Identifier ICON, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground, float scale) {
        switch (displayMode) {
            case FRACTIONAL -> drawDurabilityNumber(ICON, x, y, u, v, textureWidth, textureHeight, iconWidth, iconHeight, drawBackground, scale);
            case BAR -> drawDurabilityBar(ICON, x, y, u, v, textureWidth, textureHeight, iconWidth, iconHeight, drawBackground, scale);
            case VALUE_ONLY, PERCENTAGE -> RenderUtils.drawSmallHUD(str, x, y, getWidth(), getHeight(), ICON, u, v, textureWidth, textureHeight, iconWidth, iconHeight, durabilityColor, iconColor, hudDisplayMode, drawBackground, scale);
            case COMPACT -> drawDurabilityCompact(ICON, x, y, u, v, textureWidth, textureHeight, iconWidth, iconHeight, drawBackground, scale);
        }
    }

    public void drawDurabilityBar(Identifier ICON, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground, float scale) {
        int w = getWidth();
        int h = getHeight();

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (hudDisplayMode) {
            case ICON -> {
                if (drawBackground)
                    RenderUtils.fillRounded(x, y, x + iconWidth, y + iconHeight, 0x80000000, scale);
                RenderUtils.drawTexture(ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor, scale);
            }
            case INFO -> {
                if (drawBackground)
                    RenderUtils.fillRounded(x, y, x + w, y + h, 0x80000000, scale);
                RenderUtils.drawTexture(DURABILITY_BACKGROUND_TEXTURE, x + padding, y + 3, 0.0F, 0.0F, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, scale);
                if (step != 0) RenderUtils.drawTexture(DURABILITY_TEXTURE, x + padding, y + 3, 0.0F, 0.0F, 4 * step, DURABILITY_TEXTURE_HEIGHT, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, this.durabilityColor, scale);
            }
            case BOTH -> { // WIP INFO_ONLY bruh
                // draw the background
                if (drawBackground) {
                    if (gap <= 0)
                        RenderUtils.fillRounded(x, y, x + w, y + h, 0x80000000, scale);
                    else {
                        RenderUtils.fillRoundedLeftSide(x, y, x + iconWidth, y + h, 0x80000000, scale);
                        RenderUtils.fillRoundedRightSide(x + iconWidth + gap, y, x + w, y + h, 0x80000000, scale);
                    }
                }

                RenderUtils.drawTexture(ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor, scale);

                // draw the durability background and steps
                RenderUtils.drawTexture(DURABILITY_BACKGROUND_TEXTURE, x + iconWidth + gap + padding, y + 3, 0.0F, 0.0F, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, scale);
                if (step != 0) RenderUtils.drawTexture(DURABILITY_TEXTURE, x + iconWidth + gap + padding, y + 3, 0.0F, 0.0F, 4 * step, DURABILITY_TEXTURE_HEIGHT, DURABILITY_TEXTURE_WIDTH, DURABILITY_TEXTURE_HEIGHT, this.durabilityColor, scale);
            }
        }
    }

    // example render: ¹²³⁴/₅₆₇₈
    public void drawDurabilityNumber(Identifier ICON, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground, float scale) {
        int w = getWidth();
        int h = getHeight();

        int padding = HUD_SETTINGS.textPadding;
        int gap = HUD_SETTINGS.iconInfoGap;

        switch (hudDisplayMode) {
            case ICON -> {
                if (drawBackground)
                    RenderUtils.fillRounded(x, y, x + iconWidth, y + iconHeight, 0x80000000, scale);
                RenderUtils.drawTexture(ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor, scale);
            }
            case INFO -> {
                if (drawBackground)
                    RenderUtils.fillRounded(x, y, x + w, y + h, 0x80000000, scale);
                RenderUtils.drawText(str, x + padding, y + 3, durabilityColor, false, scale);
                RenderUtils.drawText(str2, x + padding + remainingTextWidth, y + 3 - 1, durabilityColor, false, scale);
            }
            case BOTH ->  {

                if (drawBackground) {
                    if (gap <= 0)
                        RenderUtils.fillRounded(x, y, x + w, y + h, 0x80000000, scale);
                    else {
                        RenderUtils.fillRoundedLeftSide(x, y, x + iconWidth, y + h, 0x80000000, scale);
                        RenderUtils.fillRoundedRightSide(x + iconWidth + gap, y, x + w, y + h, 0x80000000, scale);
                    }
                }
                RenderUtils.drawTexture(ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor, scale);

                RenderUtils.drawText(str, x + iconWidth + gap + padding, y + 3, durabilityColor, false, scale);
                RenderUtils.drawText(str2, x + iconWidth + gap + padding + remainingTextWidth, y + 3 - 1, durabilityColor, false, scale);
            }
        }
    }

    public void drawDurabilityCompact(Identifier ICON, int x, int y, float u, float v, int textureWidth, int textureHeight, int iconWidth, int iconHeight, boolean drawBackground, float scale) {

        if (drawBackground)
            RenderUtils.fillRounded(x, y, x + iconWidth, y + iconHeight, 0x80000000, scale);

        if (hudDisplayMode != INFO) {
            RenderUtils.drawTexture(ICON, x, y, u, v, iconWidth, iconHeight, textureWidth, textureHeight, iconColor, scale);
        }

        if (hudDisplayMode != HUDDisplayMode.ICON) {
            int durabilityWidth = 9;
            int firstX = x + ((iconWidth - durabilityWidth) / 2);
            int firstY = y + iconHeight - 2;

            RenderUtils.fill(firstX, firstY, firstX + durabilityWidth, firstY + 1, 0x80000000, scale);
            RenderUtils.fill(firstX, firstY, firstX + step, firstY + 1, durabilityColor, scale);
        }
    }
}
