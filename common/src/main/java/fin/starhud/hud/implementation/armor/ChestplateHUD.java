package fin.starhud.hud.implementation.armor;

import fin.starhud.Main;
import fin.starhud.config.hud.armor.ArmorSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class ChestplateHUD extends AbstractArmorHUD {
    private static final ArmorSettings SETTINGS = Main.settings.armorSettings.chestplate;
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/chestplate.png");

    public ChestplateHUD() {
        super(SETTINGS, TEXTURE, EquipmentSlot.CHEST);
    }

    @Override
    public String getName() {
        return "Chestplate HUD";
    }

    @Override
    public String getId() {
        return HUDId.CHESTPLATE.toString();
    }
}
