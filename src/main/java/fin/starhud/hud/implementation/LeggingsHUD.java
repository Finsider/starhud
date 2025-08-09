package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.ArmorSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.util.Identifier;

public class LeggingsHUD extends AbstractArmorHUD {

    private static final ArmorSettings SETTINGS = Main.settings.armorSettings.leggings;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/leggings.png");

    public LeggingsHUD() {
        super(SETTINGS, TEXTURE, 1);
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
