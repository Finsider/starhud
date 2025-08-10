package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.hud.InventorySettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class InventoryHUD extends AbstractHUD {

    private static final InventorySettings INVENTORY_SETTINGS = Main.settings.inventorySettings;
    private static final GeneralSettings.HUDSettings HUD_SETTINGS = Main.settings.generalSettings.hudSettings;

    private static final Identifier INVENTORY_TEXTURE = Identifier.of("starhud", "hud/inventory.png");
    private static final Identifier INVENTORY_TEXTURE_VERTICAL = Identifier.of("starhud", "hud/inventory_vertical.png");

    private static final int[] SLOT_X_HORIZONTAL = new int[27];
    private static final int[] SLOT_Y_HORIZONTAL = new int[27];

    private static final int[] SLOT_X_VERTICAL = new int[27];
    private static final int[] SLOT_Y_VERTICAL = new int[27];

    // 9 inventory slots + 8 for each gap.
    private static final int TEXTURE_WIDTH_HORIZONTAL = 22 * 9 + 8;
    private static final int TEXTURE_HEIGHT_HORIZONTAL = 68;

    private static final int TEXTURE_WIDTH_VERTICAL = 68;
    private static final int TEXTURE_HEIGHT_VERTICAL = 22 * 9 + 8;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    static {
        preComputeHorizontal();
        preComputeVertical();
    }

    private int width;
    private int height;
    private boolean drawVertical;

    public int getTotalLargerLength() {
        return (22 * 9) + (Math.min(HUD_SETTINGS.iconInfoGap, 1) * 8);
    }

    public int getTotalSmallerLength() {
        return (22 * 3) + (Math.min(HUD_SETTINGS.iconInfoGap, 1) * 2);
    }

    @Override
    public boolean collectHUDInformation() {
        if (hasItemInInventory()) {
            drawVertical = INVENTORY_SETTINGS.drawVertical;
            width = drawVertical ? getTotalSmallerLength() : getTotalLargerLength();
            height = drawVertical ? getTotalLargerLength() : getTotalSmallerLength();

            setWidthHeight(width, height);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return "Inventory HUD";
    }

    @Override
    public String getId() {
        return HUDId.INVENTORY.toString();
    }

    public InventoryHUD() {
        super(INVENTORY_SETTINGS.base);
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {
        if (drawVertical) {
            return drawInventoryVertical(context, x, y, drawBackground);
        } else {
            return drawInventoryHorizontal(context, x, y, drawBackground);
        }
    }

    @Override
    public void update() {
        super.update();

        preComputeHorizontal();
        preComputeVertical();
    }

    private boolean drawInventoryVertical(DrawContext context, int x, int y, boolean drawBackground) {
        PlayerInventory inventory = CLIENT.player.getInventory();

        int w = getWidth();
        int h = getHeight();
        int gap = HUD_SETTINGS.iconInfoGap;

        if (drawBackground) {
            if (gap <= 0) {
                RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
            } else {
                RenderUtils.drawTextureHUD(context, INVENTORY_TEXTURE_VERTICAL, x, y, 0.0F, 0.0F, TEXTURE_WIDTH_VERTICAL, TEXTURE_HEIGHT_VERTICAL, TEXTURE_WIDTH_VERTICAL, TEXTURE_HEIGHT_VERTICAL);
            }
        }

        for (int itemIndex = 0; itemIndex < 27; ++itemIndex) {

            ItemStack stack = inventory.getMainStacks().get(itemIndex + 9);

            if (!stack.isEmpty()) {

                int x1 = x + SLOT_X_VERTICAL[itemIndex];
                int y1 = y + SLOT_Y_VERTICAL[itemIndex];

                context.drawItem(stack, x1, y1);
                context.drawStackOverlay(CLIENT.textRenderer, stack, x1, y1);
            }
        }

        return true;
    }

    private boolean drawInventoryHorizontal(DrawContext context, int x, int y, boolean drawBackground) {
        PlayerInventory inventory = CLIENT.player.getInventory();

        int w = getWidth();
        int h = getHeight();
        int gap = HUD_SETTINGS.iconInfoGap;

        if (drawBackground) {
            if (gap <= 0) {
                RenderUtils.fillRounded(context, x, y, x + w, y + h, 0x80000000);
            } else {
                RenderUtils.drawTextureHUD(context, INVENTORY_TEXTURE, x, y, 0.0F, 0.0F, TEXTURE_WIDTH_HORIZONTAL, TEXTURE_HEIGHT_HORIZONTAL, TEXTURE_WIDTH_HORIZONTAL, TEXTURE_HEIGHT_HORIZONTAL);
            }

        }

        for (int itemIndex = 0; itemIndex < 27; ++itemIndex) {

            ItemStack stack = inventory.getMainStacks().get(itemIndex + 9);

            if (!stack.isEmpty()) {
                int x1 = x + SLOT_X_HORIZONTAL[itemIndex];
                int y1 = y + SLOT_Y_HORIZONTAL[itemIndex];

                context.drawItem(stack, x1, y1);
                context.drawStackOverlay(CLIENT.textRenderer, stack, x1, y1);
            }
        }

        return true;
    }

    public boolean hasItemInInventory() {
        PlayerInventory inventory = CLIENT.player.getInventory();
        for (int itemIndex = 0; itemIndex < 27; ++itemIndex) {
            ItemStack stack = inventory.getMainStacks().get(itemIndex + 9);
            if (!stack.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private static void preComputeHorizontal() {

        int gap = 3 + 16 + 3 + Math.min(HUD_SETTINGS.iconInfoGap, 1);

        int x1 = 3;

        // start y1 on index -1 (before start)
        int y1 = 3 - gap;

        for (int i = 0; i < 27; ++i) {
            if (i % 9 == 0) {
                y1 += gap;
                x1 = 3;
            }
            SLOT_X_HORIZONTAL[i] = x1;
            SLOT_Y_HORIZONTAL[i] = y1;
            x1 += gap;
        }
    }

    private static void preComputeVertical() {

        int gap = 3 + 16 + 3 + Math.min(HUD_SETTINGS.iconInfoGap, 1);
        // start the first row on the right-most column, hence the 23 + 23.
        // adds another 23 to start on index -1 (before start).
        int x1 = 3 + gap + gap + gap;
        int y1 = 3;

        for (int i = 0; i < 27; ++i) {
            if (i % 9 == 0) {
                y1 = 3;
                x1 -= gap;
            }

            SLOT_X_VERTICAL[i] = x1;
            SLOT_Y_VERTICAL[i] = y1;

            y1 += gap;
        }
    }
}
