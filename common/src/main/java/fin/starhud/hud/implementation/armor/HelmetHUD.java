package fin.starhud.hud.implementation.armor;

import fin.starhud.Main;
import fin.starhud.config.hud.armor.ArmorSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class HelmetHUD extends AbstractArmorHUD {

    private static final ArmorSettings SETTINGS = Main.settings.armorSettings.helmet;
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/helmet.png");

    public HelmetHUD() {
        super(SETTINGS, TEXTURE, EquipmentSlot.HEAD);
    }

    @Override
    public String getName() {
        return "Helmet HUD";
    }

    @Override
    public String getId() {
        return HUDId.HELMET.toString();
    }
}
