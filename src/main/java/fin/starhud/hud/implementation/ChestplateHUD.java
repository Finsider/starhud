package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.ArmorSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.util.Identifier;

public class ChestplateHUD extends AbstractArmorHUD {
    private static final ArmorSettings SETTINGS = Main.settings.armorSettings.chestplate;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/chestplate.png");

    public ChestplateHUD() {
        super(SETTINGS, TEXTURE, 2);
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
