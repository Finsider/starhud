package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class inventory {

    private static final Identifier INVENTORY_TEXTURE = Identifier.of("starhud", "hud/inventory.png");
    private static final Identifier INVENTORY_TEXTURE_VERTICAL = Identifier.of("starhud", "hud/inventory_vertical.png");

    private static final int[] SLOT_X_HORIZONTAL = new int[27];
    private static final int[] SLOT_Y_HORIZONTAL = new int[27];

    private static final int[] SLOT_X_VERTICAL = new int[27];
    private static final int[] SLOT_Y_VERTICAL = new int[27];

    private static final Settings.InventorySettings inventory = Main.settings.inventorySettings;

    private static final int width = 206;
    private static final int height = 68;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    static {
        preComputeHorizontal();
        preComputeVertical();
    }

    public static void renderInventoryHUD(DrawContext context) {
        if ((inventory.hideOn.f3 && Helper.isDebugHUDOpen()) || (inventory.hideOn.chat && Helper.isChatFocused())) return;

        PlayerInventory playerInventory = client.player.getInventory();

        context.getMatrices().push();
        Helper.setHUDScale(context, inventory.scale);

        if (inventory.drawVertical) {
            int x = Helper.calculatePositionX(inventory.x, inventory.originX, height, inventory.scale);
            int y = Helper.calculatePositionY(inventory.y, inventory.originY, width, inventory.scale);
            drawInventoryVertical(playerInventory, client.textRenderer, context, x, y);
        } else {
            int x = Helper.calculatePositionX(inventory.x, inventory.originX, width, inventory.scale);
            int y = Helper.calculatePositionY(inventory.y, inventory.originY, height, inventory.scale);
            drawInventoryHorizontal(playerInventory, client.textRenderer, context, x, y);
        }

        context.getMatrices().pop();
    }

    private static void drawInventoryVertical(PlayerInventory inventory, TextRenderer textRenderer, DrawContext context, int x, int y) {
        boolean foundItem = false;

        for (int i = 0; i < 27; ++i) {

            ItemStack stack = inventory.main.get(i + 9);

            if (!stack.isEmpty()) {

                if (!foundItem) {
                    foundItem = true;
                    context.drawTexture(RenderLayer::getGuiTextured, INVENTORY_TEXTURE_VERTICAL, x, y, 0.0F, 0.0F, height, width, height, width);
                }

                int x1 = x + SLOT_X_VERTICAL[i];
                int y1 = y + SLOT_Y_VERTICAL[i];

                context.drawItem(stack, x1, y1);
                context.drawStackOverlay(textRenderer, stack, x1, y1);
            }
        }
    }

    private static void drawInventoryHorizontal(PlayerInventory inventory, TextRenderer textRenderer, DrawContext context, int x, int y) {
        boolean foundItem = false;
        for (int i = 0; i < 27; ++i) {

            ItemStack stack = inventory.main.get(i + 9);

            if (!stack.isEmpty()) {

                if (!foundItem) {
                    foundItem = true;
                    context.drawTexture(RenderLayer::getGuiTextured, INVENTORY_TEXTURE, x, y, 0.0F, 0.0F, width, height, width, height);
                }

                int x1 = x + SLOT_X_HORIZONTAL[i];
                int y1 = y + SLOT_Y_HORIZONTAL[i];

                context.drawItem(stack, x1, y1);
                context.drawStackOverlay(textRenderer, stack, x1, y1);
            }
        }
    }

    private static void preComputeHorizontal() {
        int x1 = 3;
        // 1 offset
        int y1 = -20;

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
        // 72 = 23 + 23 + 23 + 3 (2 items to the right + 1 offset)
        int x1 = 72;
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
}
