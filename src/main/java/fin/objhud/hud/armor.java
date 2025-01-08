package fin.objhud.hud;

import fin.objhud.Helper;
import fin.objhud.Main;
import fin.objhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class armor {

    private static Settings.ArmorSettings armor = Main.settings.armorSettings;

    private static final Identifier ARMOR_BACKGROUND_TEXTURE = Identifier.of("objhud", "hud/armor.png");
    private static final Identifier ARMOR_ICONS_TEXTURE = Identifier.of("objhud", "hud/armor_icons.png");

    public static void renderArmorHUD(DrawContext context) {
        if (!armor.renderArmorHUD) return;

        MinecraftClient client = MinecraftClient.getInstance();

        int x = Helper.defaultHUDLocationX(armor.defX, context) + armor.x;
        int y = Helper.defaultHUDLocationY(armor.defY, context) + armor.y;

        int i = 3;
        // for each armor pieces
        for (ItemStack armor : client.player.getArmorItems()) {
            if (armor.isItemBarVisible())
                renderArmorPieces(context, armor, x, y, 14 * i);
            --i;
        }
    }

    // oh.
    public static void renderArmorPieces(DrawContext context, ItemStack armor, int x, int y, int gap) {
        int step = getItemBarStep(armor);
        int color = getItemBarColor(step) | 0xFF000000;
        // draw the background
        context.drawTexture(ARMOR_BACKGROUND_TEXTURE, x, y + gap, 0, gap, 63, 13, 63 ,55);
        // draw the information
        Helper.drawTextureColor(context, ARMOR_ICONS_TEXTURE, x + 19, y + 3 + gap, 0, 0, (4 * step), 7, 40, 7, color);
    }

    // get the durability "steps" or progress.
    public static int getItemBarStep(ItemStack stack) {
        return MathHelper.clamp(Math.round(10 - (float)stack.getDamage() * 10 / (float)stack.getMaxDamage()), 0, 10);
    }

    // color transition from pastel (red to green).
    public static int getItemBarColor(int stackStep) {
        return MathHelper.hsvToRgb(0.35F * stackStep / 10.0F, 0.45F, 0.95F);
    }

}