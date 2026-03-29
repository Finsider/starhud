package fin.starhud.hud.implementation.armor;

import fin.starhud.Main;
import fin.starhud.config.hud.DurabilitySettings;
import fin.starhud.config.hud.armor.ArmorSettings;
import fin.starhud.hud.implementation.AbstractDurabilityHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractArmorHUD extends AbstractDurabilityHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final DurabilitySettings DURABILITY_SETTINGS = Main.settings.armorSettings.durabilitySettings;

    private final ArmorSettings SETTINGS;
    private final Identifier TEXTURE;
    private EquipmentSlot slot;

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    public AbstractArmorHUD(ArmorSettings armorSettings, Identifier armorTexture, EquipmentSlot slot) {
        super(armorSettings.base, DURABILITY_SETTINGS);
        this.SETTINGS = armorSettings;
        this.TEXTURE = armorTexture;
        this.slot = slot;
    }

    @Override
    public ItemStack getStack() {
        if (CLIENT.player == null) return null;

        return CLIENT.player.getItemBySlot(this.slot);
    }

    @Override
    public int getIconColor() {
        return SETTINGS.color | 0xFF000000;
    }

    @Override
    public boolean renderHUD(GuiGraphicsExtractor context, int x, int y, boolean drawBackground, boolean drawTextShadow) {
        return renderDurabilityHUD(
                context,
                TEXTURE,
                x, y,
                0.0F, 0.0F,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                drawBackground,
                drawTextShadow
        );
    }
}
