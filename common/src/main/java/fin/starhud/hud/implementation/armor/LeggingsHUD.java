package fin.starhud.hud.implementation.armor;

import fin.starhud.Main;
import fin.starhud.config.hud.armor.ArmorSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class LeggingsHUD extends AbstractArmorHUD {

    private static final ArmorSettings SETTINGS = Main.settings.armorSettings.leggings;
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/leggings.png");

    public LeggingsHUD() {
        super(SETTINGS, TEXTURE, EquipmentSlot.LEGS);
    }

    @Override
    public String getName() {
        return "Leggings HUD";
    }

    @Override
    public String getId() {
        return HUDId.LEGGINGS.toString();
    }

}
