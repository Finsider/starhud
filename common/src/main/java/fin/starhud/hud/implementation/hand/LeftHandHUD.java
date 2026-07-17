package fin.starhud.hud.implementation.hand;

import fin.starhud.Main;
import fin.starhud.config.hud.hand.HandSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;

public class LeftHandHUD extends AbstractHandHUD {

    private static final HandSettings LEFT_HAND_SETTINGS = Main.settings.handSettings.leftHandSettings;
    private static final Identifier LEFT_HAND_TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/hand_left.png");

    public LeftHandHUD() {
        super(LEFT_HAND_SETTINGS, HumanoidArm.LEFT, LEFT_HAND_TEXTURE);
    }

    @Override
    public String getName() {
        return "Left Hand HUD";
    }

    @Override
    public String getId() {
        return HUDId.LEFT_HAND.toString();
    }

    @Override
    public int getIconColor() {
        return LEFT_HAND_SETTINGS.color | 0xFF000000;
    }
}
