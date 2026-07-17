package fin.starhud.condition;

import fin.starhud.hud.HUDComponent;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.other.TargetedCrosshairHUD;

public class TargetedCrosshair {

    private static final TargetedCrosshairHUD TARGETED_CROSSHAIR_HUD = (TargetedCrosshairHUD) HUDComponent.getInstance().getHUD(HUDId.TARGETED_CROSSHAIR);

    public static boolean isShown(String ignored) {
        return TargetedCrosshairHUD.isShown();
    }

    public static int getWidth() {
        return TARGETED_CROSSHAIR_HUD.getWidth();
    }
    public static int getHeight() {
        return TARGETED_CROSSHAIR_HUD.getHeight();
    }
}
