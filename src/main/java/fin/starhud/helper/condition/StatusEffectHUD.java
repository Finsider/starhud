package fin.starhud.helper.condition;

import fin.starhud.hud.HUDComponent;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.NegativeEffectHUD;
import fin.starhud.hud.implementation.PositiveEffectHUD;

public class StatusEffectHUD {

    private static final PositiveEffectHUD POSITIVE_EFFECT_HUD = (PositiveEffectHUD) HUDComponent.getInstance().getHUD(HUDId.POSITIVE_EFFECT);
    private static final NegativeEffectHUD NEGATIVE_EFFECT_HUD = (NegativeEffectHUD) HUDComponent.getInstance().getHUD(HUDId.NEGATIVE_EFFECT);

    public static boolean isPositiveShown(String ignored) {
        return POSITIVE_EFFECT_HUD.size > 0;
    }

    public static int getPositiveWidth() {
        if (POSITIVE_EFFECT_HUD.shouldRender()) {
            return POSITIVE_EFFECT_HUD.getWidth();
        } else {
            return POSITIVE_EFFECT_HUD.size * 25 - 1;
        }
    }

    public static int getPositiveHeight() {
        if (POSITIVE_EFFECT_HUD.shouldRender()) {
            return POSITIVE_EFFECT_HUD.getHeight();
        } else {
            return 24;
        }
    }

    public static boolean isNegativeShown(String ignored) {
        return NEGATIVE_EFFECT_HUD.size > 0;
    }

    public static int getNegativeWidth() {
        if (NEGATIVE_EFFECT_HUD.shouldRender()) {
            return NEGATIVE_EFFECT_HUD.getWidth();
        } else {
            return NEGATIVE_EFFECT_HUD.size * 25 - 1;
        }
    }

    public static int getNegativeHeight() {
        if (NEGATIVE_EFFECT_HUD.shouldRender()) {
            return NEGATIVE_EFFECT_HUD.getHeight();
        } else {
            return 24;
        }
    }

}
