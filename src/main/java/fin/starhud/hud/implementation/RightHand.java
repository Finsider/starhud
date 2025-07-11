package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.HandSettings;
import net.minecraft.util.Arm;

public class RightHand extends Hand {

    private static final HandSettings RIGHT_HAND_SETTINGS = Main.settings.handSettings.rightHandSettings;

    public RightHand() {
        super(RIGHT_HAND_SETTINGS, Arm.RIGHT);
    }

    @Override
    int getV() {
        return 14;
    }
}
