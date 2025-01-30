package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
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

    private static final Settings.HandSettings.BaseHandSettings leftHand = Main.settings.handSettings.leftHandSettings.hand;
    private static final Settings.BaseSettings base_leftHand = Main.settings.handSettings.leftHandSettings.base;

    private static final Settings.HandSettings.BaseHandSettings rightHand = Main.settings.handSettings.rightHandSettings.hand;
    private static final Settings.BaseSettings base_rightHand = Main.settings.handSettings.rightHandSettings.base;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    private static int x_leftHand;
    private static int y_leftHand;

    private static int x_rightHand;
    private static int y_rightHand;

    static {
        initLeftHandConfiguration();
        initRightHandConfiguration();
    }

    public static void renderLeftHandHUD(DrawContext context) {
        renderHandHUD(context, leftHand, base_leftHand, Arm.LEFT, x_leftHand, y_leftHand);
    }

    public static void renderRightHandHUD(DrawContext context) {
        renderHandHUD(context, rightHand, base_rightHand, Arm.RIGHT, x_rightHand, y_rightHand);
    }

    private static void renderHandHUD(DrawContext context, Settings.HandSettings.BaseHandSettings hand, Settings.BaseSettings base, Arm arm, int x, int y) {
        if (Helper.isHideOn(base.hideOn)) return;

        PlayerInventory playerInventory = client.player.getInventory();

        ItemStack stack = getItemInHand(playerInventory, arm);
        if (stack.isEmpty()) return;

        float v = arm == Arm.RIGHT ? 14 : 0;

        // either draw the durability or the amount of item in the inventory.
        Helper.renderHUD(context, base.scale, () -> {
            if (hand.showDurability && stack.isDamageable()) {
                int x1 = x - Helper.getGrowthDirection(hand.textureGrowth, width_durability);
                Helper.renderItemDurabilityHUD(context, HAND_TEXTURE, stack, x1, y, v, width_count + width, 27, hand.color | 0xFF000000);
            } else if (hand.showCount) {
                int x1 = x - Helper.getGrowthDirection(hand.textureGrowth, width_count);
                renderItemCountHUD(context, client.textRenderer, playerInventory, stack, x1, y, v, hand.color | 0xFF000000);
            }
        });
    }

    private static ItemStack getItemInHand(PlayerInventory playerInventory, Arm arm) {
        return client.player.getMainArm() == arm ? playerInventory.getMainHandStack() : playerInventory.offHand.get(0);
    }

    private static void renderItemCountHUD(DrawContext context, TextRenderer textRenderer, PlayerInventory playerInventory, ItemStack stack, int x, int y, float v, int color) {
        int stackAmount = getItemCount(playerInventory, stack);

        context.drawTexture(RenderLayer::getGuiTextured, HAND_TEXTURE, x, y, 0.0F, v, width_count + width, height, width_count + width, 27, color);
        context.drawText(textRenderer, Integer.toString(stackAmount), x + 19, y + 3, color, false);
    }

    private static int getItemCount(PlayerInventory inventory, ItemStack stack) {
        int stackAmount = 0;

        ItemStack offHand = inventory.offHand.get(0);
        if (!offHand.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack, offHand))
            stackAmount += offHand.getCount();

        for (ItemStack item : inventory.main)
            if (!item.isEmpty() && ItemStack.areItemsAndComponentsEqual(item, stack))
                stackAmount += item.getCount();

        if (stack.getItem() instanceof ArmorItem) {
        for (ItemStack item : inventory.armor)
            if (!item.isEmpty() && ItemStack.areItemsAndComponentsEqual(item, stack))
                stackAmount += item.getCount();
        }

        return stackAmount;
    }

    public static void initLeftHandConfiguration() {
        x_leftHand = Helper.calculatePositionX(base_leftHand, width);
        y_leftHand = Helper.calculatePositionY(base_leftHand, height);
    }

    public static void initRightHandConfiguration() {
        x_rightHand = Helper.calculatePositionX(base_rightHand, width);
        y_rightHand = Helper.calculatePositionY(base_rightHand, height);
    }
}