package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.HandSettings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Arm;

public class LeftHand extends Hand{

    private static final HandSettings LEFT_HAND_SETTINGS = Main.settings.handSettings.leftHandSettings;

    public LeftHand() {
        super(LEFT_HAND_SETTINGS);
    }

    @Override
    int getV() {
        return 0;
    }

    @Override
    public void renderHUD(DrawContext context) {
        renderHandHUD(context, Arm.LEFT, x, y);
    }
}
