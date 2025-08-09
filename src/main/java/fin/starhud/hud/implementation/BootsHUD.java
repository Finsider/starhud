package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.ArmorSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.util.Identifier;

public class BootsHUD extends AbstractArmorHUD {

    private static final ArmorSettings SETTINGS = Main.settings.armorSettings.boots;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/boots.png");

    public BootsHUD() {
        super(SETTINGS, TEXTURE, 0);
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
