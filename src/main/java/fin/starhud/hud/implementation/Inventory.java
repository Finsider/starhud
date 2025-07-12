package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.InventorySettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Inventory extends AbstractHUD {

    private static final InventorySettings INVENTORY_SETTINGS = Main.settings.inventorySettings;
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

    @Override
    public String getName() {
        return "Inventory HUD";
    }

    public Inventory() {
        super(INVENTORY_SETTINGS.base);
    }

    @Override
    public void renderHUD(DrawContext context) {
        if (INVENTORY_SETTINGS.drawVertical) {
            drawInventoryVertical(context, x, y);
        } else {
            drawInventoryHorizontal(context, x, y);
        }
    }

    private void drawInventoryVertical(DrawContext context, int x, int y) {
        PlayerInventory inventory = CLIENT.player.getInventory();
        boolean foundItem = false;

        for (int itemIndex = 0; itemIndex < 27; ++itemIndex) {

            ItemStack stack = inventory.getMainStacks().get(itemIndex + 9);

            if (!stack.isEmpty()) {

                if (!foundItem) {
                    foundItem = true;
                    RenderUtils.drawTextureHUD(context, INVENTORY_TEXTURE_VERTICAL, x, y, 0.0F, 0.0F, TEXTURE_WIDTH_VERTICAL, TEXTURE_HEIGHT_VERTICAL, TEXTURE_WIDTH_VERTICAL, TEXTURE_HEIGHT_VERTICAL);
                }

                int x1 = x + SLOT_X_VERTICAL[itemIndex];
                int y1 = y + SLOT_Y_VERTICAL[itemIndex];

                context.drawItem(stack, x1, y1);
                context.drawStackOverlay(CLIENT.textRenderer, stack, x1, y1);
            }
        }

        setBoundingBox(x, y, TEXTURE_WIDTH_VERTICAL, TEXTURE_HEIGHT_VERTICAL);
    }

    private void drawInventoryHorizontal(DrawContext context, int x, int y) {
        PlayerInventory inventory = CLIENT.player.getInventory();
        boolean foundItem = false;

        for (int itemIndex = 0; itemIndex < 27; ++itemIndex) {

            ItemStack stack = inventory.getMainStacks().get(itemIndex + 9);

            if (!stack.isEmpty()) {

                if (!foundItem) {
                    foundItem = true;
                    RenderUtils.drawTextureHUD(context, INVENTORY_TEXTURE, x, y, 0.0F, 0.0F, TEXTURE_WIDTH_HORIZONTAL, TEXTURE_HEIGHT_HORIZONTAL, TEXTURE_WIDTH_HORIZONTAL, TEXTURE_HEIGHT_HORIZONTAL);
                }

                int x1 = x + SLOT_X_HORIZONTAL[itemIndex];
                int y1 = y + SLOT_Y_HORIZONTAL[itemIndex];

                context.drawItem(stack, x1, y1);
                context.drawStackOverlay(CLIENT.textRenderer, stack, x1, y1);
            }
        }

        setBoundingBox(x, y, TEXTURE_WIDTH_HORIZONTAL, TEXTURE_HEIGHT_HORIZONTAL);
    }

    private static void preComputeHorizontal() {
        int x1 = 3;

        // start y1 on index -1 (before start)
        int y1 = 3 - 23;

        for (int i = 0; i < 27; ++i) {
            if (i % 9 == 0) {
                y1 += 23;
                x1 = 3;
            }
            SLOT_X_HORIZONTAL[i] = x1;
            SLOT_Y_HORIZONTAL[i] = y1;
            x1 += 23;
        }
    }

    private static void preComputeVertical() {
        // start the first row on the right-most column, hence the 23 + 23.
        // adds another 23 to start on index -1 (before start).
        int x1 = 3 + 23 + 23 + 23;
        int y1 = 3;

        for (int i = 0; i < 27; ++i) {
            if (i % 9 == 0) {
                y1 = 3;
                x1 -= 23;
            }

            SLOT_X_VERTICAL[i] = x1;
            SLOT_Y_VERTICAL[i] = y1;

            y1 += 23;
        }
    }
    @Override
    public int getBaseHUDWidth() {
        return INVENTORY_SETTINGS.drawVertical ? TEXTURE_WIDTH_VERTICAL : TEXTURE_WIDTH_HORIZONTAL;
    }

    @Override
    public int getBaseHUDHeight() {
        return INVENTORY_SETTINGS.drawVertical ? TEXTURE_HEIGHT_VERTICAL : TEXTURE_HEIGHT_HORIZONTAL;
    }
}
