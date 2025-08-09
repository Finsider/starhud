package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.ArmorSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.util.Identifier;

public class HelmetHUD extends AbstractArmorHUD {

    private static final ArmorSettings SETTINGS = Main.settings.armorSettings.helmet;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/helmet.png");

    public HelmetHUD() {
        super(SETTINGS, TEXTURE, 3);
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
