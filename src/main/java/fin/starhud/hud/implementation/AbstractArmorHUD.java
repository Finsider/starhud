package fin.starhud.hud.implementation;

import fin.starhud.config.hud.ArmorSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public abstract class AbstractArmorHUD extends AbstractDurabilityHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private final ArmorSettings SETTINGS;
    private final Identifier TEXTURE;
    private final int armorIndex;

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    public AbstractArmorHUD(ArmorSettings armorSettings, Identifier armorTexture, int armorIndex) {
        super(armorSettings.base, armorSettings.durabilitySettings);
        this.SETTINGS = armorSettings;
        this.TEXTURE = armorTexture;
        this.armorIndex = armorIndex;
    }

    @Override
    public ItemStack getStack() {
        EquipmentSlot equipmentSlot = AttributeModifierSlot.ARMOR.getSlots().get(armorIndex);

        if (CLIENT.player == null) return null;

        return CLIENT.player.getEquippedStack(equipmentSlot);
    }

    @Override
    public int getIconColor() {
        return SETTINGS.color | 0xFF000000;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {
        return renderDurabilityHUD(
                context,
                TEXTURE,
                x, y,
                0.0F, 0.0F,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                drawBackground
        );
    }
}
