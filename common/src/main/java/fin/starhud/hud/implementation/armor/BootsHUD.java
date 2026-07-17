package fin.starhud.hud.implementation.armor;

import fin.starhud.Main;
import fin.starhud.config.hud.armor.ArmorSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class BootsHUD extends AbstractArmorHUD {

    private static final ArmorSettings SETTINGS = Main.settings.armorSettings.boots;
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/boots.png");

    public BootsHUD() {
        super(SETTINGS, TEXTURE, EquipmentSlot.FEET);
    }

    @Override
    public String getName() {
        return "Boots HUD";
    }

    @Override
    public String getId() {
        return HUDId.BOOTS.toString();
    }

}
