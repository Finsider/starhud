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
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class hand {

    private static final Identifier HAND_TEXTURE = Identifier.of("starhud", "hud/hand.png");

    // base HUD Width (Icon width = 13, 1 for the gap between icon and text, 10 for the padding at left / right edge).
    private static final int width = 24;
    // 3 x 10 (10 durability bars) + 9 for each gap.
    // durability adds 39 additional width.
    private static final int width_durability = 39;
    // count string is at max 4 digits, each digit may have 5 pixels.
    // 5 + 1 + 5 + 1 + 5 + 1 + 5 = 23.
    // count string adds 23 additional width.
    private static final int width_count = 23;
    private static final int height = 13;

    private static final Settings.HandSettings.LeftHandSettings leftHand = Main.settings.handSettings.leftHandSettings;
    private static final Settings.BaseSettings base_leftHand = leftHand.base;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void renderLeftHandHUD(DrawContext context) {
        if (Helper.IsHideOn(base_leftHand.hideOn)) return;

        PlayerInventory playerInventory = client.player.getInventory();

        ItemStack item;
        if (client.player.getMainArm() == Arm.LEFT)
            item = playerInventory.getMainHandStack();
        else
            item = playerInventory.offHand.get(0);

        if (item.isEmpty()) return;

        context.getMatrices().push();
        Helper.setHUDScale(context, base_leftHand.scale);

        // either draw the durability or the amount of item in the inventory.
        if (leftHand.showDurability && item.isDamageable()) {
            int x = Helper.calculatePositionX(base_leftHand, width)
                    - Helper.getGrowthDirection(leftHand.textureGrowth, width_durability);
            int y = Helper.calculatePositionY(base_leftHand, height);
            // Use width_count + width because we are using the same texture used in Count HUD, We don't have the texture for durability.
            Helper.renderItemDurabilityHUD(context, HAND_TEXTURE, item, x, y, 0, width_count + width, 27, leftHand.color | 0xFF000000);
        } else if (leftHand.showCount) {
            int x = Helper.calculatePositionX(base_leftHand, width)
                    - Helper.getGrowthDirection(leftHand.textureGrowth, width_count);
            int y = Helper.calculatePositionY(base_leftHand, height);
            renderItemCountHUD(context, client.textRenderer, playerInventory, item, x, y, 0, leftHand.color | 0xFF000000);
        }

        context.getMatrices().pop();
    }

    private static final Settings.HandSettings.RightHandSettings rightHand = Main.settings.handSettings.rightHandSettings;
    private static final Settings.BaseSettings base_rightHand = rightHand.base;

    public static void renderRightHandHUD(DrawContext context) {
        if (Helper.IsHideOn(base_rightHand.hideOn)) return;

        PlayerInventory playerInventory = client.player.getInventory();

        ItemStack item;
        if (client.player.getMainArm() == Arm.LEFT)
            item = playerInventory.offHand.get(0);
        else
            item = playerInventory.getMainHandStack();

        if (item.isEmpty()) return;

        context.getMatrices().push();
        Helper.setHUDScale(context, base_rightHand.scale);

        // either draw the durability or the amount of item in the inventory.
        if (rightHand.showDurability && item.isDamageable()) {
            int x = Helper.calculatePositionX(base_rightHand, width)
                    - Helper.getGrowthDirection(rightHand.textureGrowth, width_durability);
            int y = Helper.calculatePositionY(base_rightHand, height);
            Helper.renderItemDurabilityHUD(context, HAND_TEXTURE, item, x, y, 14, width_count + width, 27, rightHand.color | 0xFF000000);
        } else if (rightHand.showCount) {
            int x = Helper.calculatePositionX(base_rightHand, width)
                    - Helper.getGrowthDirection(rightHand.textureGrowth, width_count);
            int y = Helper.calculatePositionY(base_rightHand, height);
            renderItemCountHUD(context, client.textRenderer, playerInventory, item, x, y, 14, rightHand.color | 0xFF000000);
        }

        context.getMatrices().pop();
    }

    private static void renderItemCountHUD(DrawContext context, TextRenderer textRenderer, PlayerInventory playerInventory, ItemStack stack, int x, int y, float v, int color) {
        int stackAmount = getItemCount(playerInventory, stack);

        context.drawTexture(RenderLayer::getGuiTextured, HAND_TEXTURE, x, y, 0.0F, v, width_count + width, height, width_count + width, 27, color);
        context.drawText(textRenderer, Integer.toString(stackAmount), x + 19, y + 3, color, false);
    }

    private static int getItemCount(PlayerInventory inventory, ItemStack stack) {
        int stackAmount = 0;

        for (ItemStack item : inventory.offHand)
            if (!item.isEmpty() && ItemStack.areItemsAndComponentsEqual(item, stack))
                stackAmount += item.getCount();

        for (ItemStack item : inventory.main)
            if (!item.isEmpty() && ItemStack.areItemsAndComponentsEqual(item, stack))
                stackAmount += item.getCount();

        for (ItemStack item : inventory.armor)
            if (!item.isEmpty() && ItemStack.areItemsAndComponentsEqual(item, stack))
                stackAmount += item.getCount();

        return stackAmount;
    }
}