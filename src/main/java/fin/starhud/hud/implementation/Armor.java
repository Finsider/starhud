package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.ArmorSettings;
import fin.starhud.helper.Box;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Armor extends AbstractHUD {

    private static final ArmorSettings ARMOR_SETTINGS = Main.settings.armorSettings;

    private static final Identifier ARMOR_BACKGROUND_TEXTURE = Identifier.of("starhud", "hud/armor.png");

    private static final ArmorSettings.ArmorPieceSetting[] PIECE_SETTINGS = {
            ARMOR_SETTINGS.helmet,
            ARMOR_SETTINGS.chestplate,
            ARMOR_SETTINGS.leggings,
            ARMOR_SETTINGS.boots
    };

    private static final int TEXTURE_WIDTH = 13 + 1 + 5 + 5;
    private static final int TEXTURE_HEIGHT = 13;

    private static final int ITEM_TEXTURE_WIDTH = 22 + 1 + 5 + 5;
    private static final int ITEM_TEXTURE_HEIGHT = 3 + 16 + 3;

    private static boolean needBoxUpdate = true;
    private static Box cachedBox = null;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public Armor() {
        super(ARMOR_SETTINGS.base);
    }

    @Override
    public String getName() {
        return "Armor HUD";
    }

    @Override
    public int getBaseHUDWidth() {
        return (PIECE_SETTINGS[0].drawItem || PIECE_SETTINGS[1].drawItem || PIECE_SETTINGS[2].drawItem || PIECE_SETTINGS[3].drawItem) ? ITEM_TEXTURE_WIDTH : TEXTURE_WIDTH;
    }

    @Override
    public int getBaseHUDHeight() {
        return (PIECE_SETTINGS[0].drawItem || PIECE_SETTINGS[1].drawItem || PIECE_SETTINGS[2].drawItem || PIECE_SETTINGS[3].drawItem) ? ITEM_TEXTURE_HEIGHT : TEXTURE_HEIGHT;
    }

    @Override
    public boolean renderHUD(DrawContext context) {
        int armorIndex = 3;

        boolean rendered = false;

        for (EquipmentSlot equipmentSlot : AttributeModifierSlot.ARMOR) {
            if (equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack armor = CLIENT.player.getEquippedStack(equipmentSlot);
                if (PIECE_SETTINGS[armorIndex].shouldRender && !armor.isEmpty() && armor.isDamageable()) {
                    Box tempBox = RenderUtils.renderDurabilityHUD(
                            context,
                            ARMOR_BACKGROUND_TEXTURE,
                            armor,
                            x + PIECE_SETTINGS[armorIndex].xOffset,
                            y + PIECE_SETTINGS[armorIndex].yOffset,
                            14 * armorIndex,
                            13,
                            TEXTURE_HEIGHT * 4 + 3,
                            PIECE_SETTINGS[armorIndex].color | 0xFF000000,
                            PIECE_SETTINGS[armorIndex].drawBar,
                            PIECE_SETTINGS[armorIndex].drawItem,
                            ARMOR_SETTINGS.base.growthDirectionX
                    );

                    if (needBoxUpdate) {
                        if (cachedBox == null) {
                            cachedBox = new Box(tempBox.getX(), tempBox.getY(), tempBox.getWidth(), tempBox.getHeight(), tempBox.getColor());
                        } else {
                            cachedBox.mergeWith(tempBox);
                        }
                    }
                    rendered = true;
                }
            }
            --armorIndex;
        }

        if (needBoxUpdate) {
            needBoxUpdate = false;
            copyBoundingBox(cachedBox);
        }
        return rendered;
    }

    @Override
    public void update() {
        super.update();

        needBoxUpdate = true;
        cachedBox = null;
    }
}
