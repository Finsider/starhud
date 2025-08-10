package fin.starhud.hud.implementation;

import fin.starhud.config.hud.HandSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

// i dont like this one.

public abstract class AbstractHandHUD extends AbstractDurabilityHUD {

    private static final Identifier ITEM_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/item.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;

    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static final int ITEM_TEXTURE_WIDTH = 3 + 16 + 3;
    private static final int ITEM_TEXTURE_HEIGHT = 3 + 16 + 3;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private final HandSettings handSettings;
    private final Arm arm;
    private final Identifier ICON_TEXTURE;

    public AbstractHandHUD(HandSettings handSettings, Arm arm, Identifier ICON_TEXTURE) {
        super(handSettings.base, handSettings.durabilitySettings);

        this.handSettings = handSettings;
        this.arm = arm;
        this.ICON_TEXTURE = ICON_TEXTURE;
    }

    @Override
    public ItemStack getStack() {
        return CLIENT.player.getStackInArm(arm);
    }

    private ItemStack item;
    private int iconColor;

    private int width;
    private int height;

    private String amountStr;

    private boolean showDurability;
    private boolean showCount;
    private boolean drawItem;

    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        item = getStack();
        if (item.isEmpty())
            return false;

        showDurability = handSettings.showDurability;
        showCount = handSettings.showCount;
        drawItem = handSettings.durabilitySettings.drawItem;
        displayMode = getSettings().getDisplayMode();

        if (showDurability && item.isDamageable()) {
            return super.collectHUDInformation();
        }

        if (!showCount)
            return false;

        iconColor = getIconColor();
        amountStr = Integer.toString(getItemCount(CLIENT.player.getInventory(), item));

        int strWidth = CLIENT.textRenderer.getWidth(amountStr) - 1;
        width = displayMode.calculateWidth((drawItem ? ITEM_TEXTURE_WIDTH : ICON_WIDTH), strWidth);
        height = drawItem ? ITEM_TEXTURE_HEIGHT : ICON_HEIGHT;

        setWidthHeightColor(width, height, iconColor);

        return true;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {
        return renderHandHUD(context, x, y, drawBackground);
    }

    public boolean renderHandHUD(DrawContext context, int x, int y, boolean drawBackground) {
        // either draw the durability or the amount of item in the inventory.
        if (showDurability && item.isDamageable()) {
            renderDurabilityHUD(
                    context,
                    ICON_TEXTURE,
                    x, y,
                    0.0F, 0.0F,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT,
                    ICON_WIDTH, ICON_HEIGHT,
                    drawBackground
            );
        } else if (showCount) {
            renderStackCountHUD(context, x, y, drawBackground);
        }

        return true;
    }

    private void renderStackCountHUD(DrawContext context, int x, int y, boolean drawBackground) {

        if (drawItem) {
            renderStackCountItemHUD(context, x, y, drawBackground);
        } else {
            renderStackCountIconHUD(context, x, y, drawBackground);
        }
    }

    private void renderStackCountItemHUD(DrawContext context, int x, int y, boolean drawBackground) {
        RenderUtils.drawItemHUD(
                context,
                amountStr,
                x, y,
                getWidth(), getHeight(),
                item,
                iconColor,
                displayMode,
                drawBackground
        );
    }

    private void renderStackCountIconHUD(DrawContext context, int x, int y, boolean drawBackground) {
        RenderUtils.drawSmallHUD(
                context,
                amountStr,
                x, y,
                getWidth(), getHeight(),
                ICON_TEXTURE,
                0.0F, 0.0F,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                iconColor,
                displayMode,
                drawBackground
        );
    }

    private static int getItemCount(PlayerInventory inventory, ItemStack stack) {
        int stackAmount = 0;

        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack item = inventory.getStack(i);
            if (!item.isEmpty() && ItemStack.areItemsAndComponentsEqual(item, stack))
                stackAmount += item.getCount();
        }

        return stackAmount;
    }
}
