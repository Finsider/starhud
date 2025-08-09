package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.HandSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class RightHandHUD extends AbstractHandHUD {

    private static final HandSettings RIGHT_HAND_SETTINGS = Main.settings.handSettings.rightHandSettings;
    private static final Identifier RIGHT_HAND_TEXTURE = Identifier.of("starhud", "hud/hand_right.png");

    public RightHandHUD() {
        super(RIGHT_HAND_SETTINGS, Arm.RIGHT, RIGHT_HAND_TEXTURE);
    }

    @Override
    public String getName() {
        return "Right Hand HUD";
    }

    @Override
    public String getId() {
        return HUDId.RIGHT_HAND.toString();
    }

    @Override
    public int getIconColor() {
        return RIGHT_HAND_SETTINGS.color | 0xFF000000;
    }
}
