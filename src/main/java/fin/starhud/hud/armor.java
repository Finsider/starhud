package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class armor {

    private static final Settings.ArmorSettings armor = Main.settings.armorSettings;

    private static final Identifier ARMOR_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/armor.png");

    private static final int[] X_OFFSETS = new int[4];
    private static final int[] Y_OFFSETS = new int[4];
    private static final boolean[] SHOULD_RENDER = new boolean[4];

    private static final int width = 63;
    private static final int height = 13;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void renderArmorHUD(DrawContext context) {
        if ((armor.hideOn.f3 && Helper.isDebugHUDOpen()) || (armor.hideOn.chat && Helper.isChatFocused())) return;

        initArmorConfiguration();

        int x = Helper.calculatePositionX(armor.x, armor.originX, width, armor.scale);
        int y = Helper.calculatePositionY(armor.y, armor.originY, height, armor.scale);
        
        int i = 3;

        context.getMatrices().push();
        Helper.setHUDScale(context, armor.scale);

        // for each armor pieces
        for (ItemStack armor : client.player.getArmorItems()) {
            if (SHOULD_RENDER[i] && !armor.isEmpty() && armor.isDamageable()) {
                Helper.renderItemDurabilityHUD(context, ARMOR_BACKGROUND_TEXTURE, armor, x + X_OFFSETS[i], y + Y_OFFSETS[i], 14 * i,13,  55, 0xFFFFFFFF);
            }
            --i;
        }

        context.getMatrices().pop();
    }

    private static void initArmorConfiguration() {
        X_OFFSETS[0] = armor.helmet.xOffset;        Y_OFFSETS[0] = armor.helmet.yOffset;
        X_OFFSETS[1] = armor.chestplate.xOffset;    Y_OFFSETS[1] = armor.chestplate.yOffset;
        X_OFFSETS[2] = armor.leggings.xOffset;      Y_OFFSETS[2] = armor.leggings.yOffset;
        X_OFFSETS[3] = armor.boots.xOffset;         Y_OFFSETS[3] = armor.boots.yOffset;

        SHOULD_RENDER[0] = armor.helmet.shouldRender;
        SHOULD_RENDER[1] = armor.chestplate.shouldRender;
        SHOULD_RENDER[2] = armor.leggings.shouldRender;
        SHOULD_RENDER[3] = armor.boots.shouldRender;
    }

}