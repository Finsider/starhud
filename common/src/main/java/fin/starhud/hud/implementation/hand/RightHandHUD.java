package fin.starhud.hud.implementation.hand;

import fin.starhud.Main;
import fin.starhud.config.hud.hand.HandSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

public class RightHandHUD extends AbstractHandHUD {

    private static final HandSettings RIGHT_HAND_SETTINGS = Main.settings.handSettings.rightHandSettings;
    private static final ResourceLocation RIGHT_HAND_TEXTURE = ResourceLocation.fromNamespaceAndPath("starhud", "hud/hand_right.png");

    public RightHandHUD() {
        super(RIGHT_HAND_SETTINGS, HumanoidArm.RIGHT, RIGHT_HAND_TEXTURE);
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
