package fin.starhud.hud.implementation;

import fin.starhud.config.hud.HandSettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

// i dont like this one.

public abstract class Hand extends AbstractHUD {

    private static final Identifier HAND_TEXTURE = Identifier.of("starhud", "hud/hand.png");

    // base HUD Width (Icon width = 13, 1 for the gap between icon and text, 5 for the gap on left side of the attribute, 5 for the gap on the right of the attribute).
    // guys i legit forgot what the "10 for the padding at left / right edge" meant help.
    private static final int TEXTURE_WIDTH = 13 + 1 + 5 + 5;

    // count string is at max 4 digits, each digit may have 5 pixels.
    // 5 + 1 + 5 + 1 + 5 + 1 + 5 = 23.
    // count string adds 23 additional width.
    private static final int COUNT_WIDTH = 23;
    private static final int TEXTURE_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private final HandSettings handSettings;

    public Hand(HandSettings handSettings) {
        super(handSettings.base);
        this.handSettings = handSettings;
    }

    public void renderHandHUD(DrawContext context, Arm arm, int x, int y) {
        PlayerInventory playerInventory = CLIENT.player.getInventory();

        ItemStack item;
        if (CLIENT.player.getMainArm() == arm)
            item = CLIENT.player.getEquippedStack(EquipmentSlot.MAINHAND);
        else
            item = CLIENT.player.getEquippedStack(EquipmentSlot.OFFHAND);

        if (item.isEmpty()) return;

        // either draw the durability or the amount of item in the inventory.
        if (handSettings.showDurability && item.isDamageable()) {
            RenderUtils.renderDurabilityHUD(context, HAND_TEXTURE, item, x, y, getV(), COUNT_WIDTH + TEXTURE_WIDTH, 27, handSettings.color | 0xFF000000, handSettings.drawBar, handSettings.textureGrowth);
        } else if (handSettings.showCount) {
            x -= handSettings.textureGrowth.getGrowthDirection(COUNT_WIDTH);
            renderItemCountHUD(context, playerInventory, item, x, y, getV(), handSettings.color | 0xFF000000);
        }
    }

    private static void renderItemCountHUD(DrawContext context, PlayerInventory playerInventory, ItemStack stack, int x, int y, float v, int color) {
        int stackAmount = getItemCount(playerInventory, stack);

        RenderUtils.drawTextureHUD(context, HAND_TEXTURE, x, y, 0.0F, v, COUNT_WIDTH + TEXTURE_WIDTH, TEXTURE_HEIGHT, COUNT_WIDTH + TEXTURE_WIDTH, 27, color);
        RenderUtils.drawTextHUD(context, Integer.toString(stackAmount), x + 19, y + 3, color, false);
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

    @Override
    public int getBaseHUDWidth() {
        return TEXTURE_WIDTH;
    }

    @Override
    public int getBaseHUDHeight() {
        return TEXTURE_HEIGHT;
    }

    // V as in the texture, where does left texture y point starts?
    // in the texture, i placed both Left and Right hand texture in the same file, but in a different place. this is just to differentiate them
    abstract int getV();

}
