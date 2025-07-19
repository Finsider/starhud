package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.HandSettings;
import net.minecraft.util.Arm;

public class RightHandHUD extends AbstractHandHUD {

    private static final HandSettings RIGHT_HAND_SETTINGS = Main.settings.handSettings.rightHandSettings;

    public RightHandHUD() {
        super(RIGHT_HAND_SETTINGS, Arm.RIGHT);
    }

    @Override
    public String getName() {
        return "Right Hand HUD";
    }

    @Override
    int getV() {
        return 14;
    }
}
