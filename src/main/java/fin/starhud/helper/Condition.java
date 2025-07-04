package fin.starhud.helper;

import fin.starhud.Helper;
import fin.starhud.hud.implementation.TargetedCrosshair;

public enum Condition {
    DEBUG_HUD_OPENED,
    CHAT_HUD_OPENED,
    BOSSBAR_SHOWN,
    SCOREBOARD_SHOWN,
    BENEFICIAL_EFFECT_SHOWN,
    HARM_EFFECT_SHOWN,
    OFFHAND_SHOWN,
    STATUS_BARS_SHOWN,
    AIR_BUBBLE_BAR_SHOWN,
    ARMOR_BAR_SHOWN,
    TARGETED_HUD_SHOWN;

    public boolean isConditionMet() {
        return switch (this) {
            case DEBUG_HUD_OPENED -> Helper.isDebugHUDOpen();
            case CHAT_HUD_OPENED -> Helper.isChatFocused();
            case BOSSBAR_SHOWN -> Helper.isBossBarShown();
            case SCOREBOARD_SHOWN -> Helper.isScoreBoardShown();
            case BENEFICIAL_EFFECT_SHOWN -> Helper.isBeneficialEffectOverlayShown();
            case HARM_EFFECT_SHOWN -> Helper.isHarmEffectOverlayShown();
            case OFFHAND_SHOWN -> Helper.isOffHandOverlayShown();
            case STATUS_BARS_SHOWN -> Helper.isStatusBarsShown();
            case AIR_BUBBLE_BAR_SHOWN -> Helper.isAirBubbleBarShown();
            case ARMOR_BAR_SHOWN -> Helper.isArmorBarShown();
            case TARGETED_HUD_SHOWN -> TargetedCrosshair.shouldHUDRender();
        };
    }
}