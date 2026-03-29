package fin.starhud.hud.implementation.hand;

import fin.starhud.Main;
import fin.starhud.config.Settings;
import fin.starhud.config.hud.hand.HandSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.implementation.AbstractDurabilityHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

// i dont like this one.

public abstract class AbstractHandHUD extends AbstractDurabilityHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final Settings.Hand SETTINGS = Main.settings.handSettings;

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;

    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static final int ITEM_TEXTURE_WIDTH = 3 + 16 + 3;
    private static final int ITEM_TEXTURE_HEIGHT = 3 + 16 + 3;

    private final HumanoidArm arm;
    private final Identifier ICON_TEXTURE;

    public AbstractHandHUD(HandSettings handSettings, HumanoidArm arm, Identifier ICON_TEXTURE) {
        super(handSettings.base, SETTINGS.durabilitySettings);

        this.arm = arm;
        this.ICON_TEXTURE = ICON_TEXTURE;
    }

    @Override
    public ItemStack getStack() {
        if (CLIENT.player == null) return null;

        return CLIENT.player.getItemHeldByArm(arm);
    }

    private ItemStack item;
    private int iconColor;

    private String amountStr;

    private boolean showDurability;
    private boolean showCount;
    private boolean drawItem;

    private boolean isItemDamagable;

    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        item = getStack();
        if (item.isEmpty())
            return false;

        showDurability = SETTINGS.showDurability;
        showCount = SETTINGS.showCount;
        drawItem = SETTINGS.durabilitySettings.drawItem;
        displayMode = getSettings().getDisplayMode();

        isItemDamagable = item.isDamageableItem();

        if (showDurability && isItemDamagable) {
            return super.collectHUDInformation();
        }

        if (!showCount)
            return false;

        iconColor = getIconColor();

        if (CLIENT.player == null) return false;

        amountStr = Integer.toString(getItemCount(CLIENT.player.getInventory(), item));

        int strWidth = CLIENT.font.width(amountStr) - 1;
        int width = displayMode.calculateWidth((drawItem ? ITEM_TEXTURE_WIDTH : ICON_WIDTH), strWidth);
        int height = drawItem ? ITEM_TEXTURE_HEIGHT : ICON_HEIGHT;

        setWidthHeightColor(width, height, iconColor);

        return amountStr != null;
    }

    @Override
    public boolean renderHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {
        return renderHandHUD(context, x, y, drawBackground, drawTextShadow);
    }

    public boolean renderHandHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {
        // either draw the durability or the amount of item in the inventory.
        if (showDurability && isItemDamagable) {
            return renderDurabilityHUD(
                    context,
                    ICON_TEXTURE,
                    x, y,
                    0.0F, 0.0F,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT,
                    ICON_WIDTH, ICON_HEIGHT,
                    drawBackground,
                    drawTextShadow
            );
        } else if (showCount) {
            return renderStackCountHUD(context, x, y, drawBackground, drawTextShadow);
        }

        return false;
    }

    private boolean renderStackCountHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {

        if (drawItem) {
            return renderStackCountItemHUD(context, x, y, drawBackground, drawTextShadow);
        } else {
            return renderStackCountIconHUD(context, x, y, drawBackground, drawTextShadow);
        }
    }

    private boolean renderStackCountItemHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {
        return RenderUtils.drawItemHUD(
                context,
                amountStr,
                x, y,
                getWidth(), getHeight(),
                item,
                iconColor,
                displayMode,
                drawBackground,
                drawTextShadow
        );
    }

    private boolean renderStackCountIconHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {
        return RenderUtils.drawSmallHUD(
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
                drawBackground,
                drawTextShadow
        );
    }

    private static int getItemCount(Inventory inventory, ItemStack stack) {
        int stackAmount = 0;

        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack item = inventory.getItem(i);
            if (!item.isEmpty() && ItemStack.isSameItemSameComponents(item, stack))
                stackAmount += item.getCount();
        }

        return stackAmount;
    }
}
