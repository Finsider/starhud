package fin.starhud.helper;

public enum Condition {
    DEBUG_HUD_OPENED,
    CHAT_HUD_OPENED,
    BOSSBAR_SHOWN,
    SCOREBOARD_SHOWN,
    BENEFICIAL_EFFECT_SHOWN,
    HARM_EFFECT_SHOWN,
    OFFHAND_SHOWN,
    HEALTH_BAR_SHOWN,
    EXPERIENCE_BAR_SHOWN,
    AIR_BUBBLE_BAR_SHOWN,
    ARMOR_BAR_SHOWN,
    TARGETED_HUD_SHOWN;

    public boolean isConditionMet() {
        return switch (this) {
            case DEBUG_HUD_OPENED -> Conditions.isDebugHUDOpen();
            case CHAT_HUD_OPENED -> Conditions.isChatFocused();
            case BOSSBAR_SHOWN -> Conditions.isBossBarShown();
            case SCOREBOARD_SHOWN -> Conditions.isScoreBoardShown();
            case BENEFICIAL_EFFECT_SHOWN -> Conditions.isBeneficialEffectOverlayShown();
            case HARM_EFFECT_SHOWN -> Conditions.isHarmEffectOverlayShown();
            case OFFHAND_SHOWN -> Conditions.isOffHandOverlayShown();
            case HEALTH_BAR_SHOWN -> Conditions.isHealthBarShown();
            case EXPERIENCE_BAR_SHOWN -> Conditions.isExperienceBarShown();
            case AIR_BUBBLE_BAR_SHOWN -> Conditions.isAirBubbleBarShown();
            case ARMOR_BAR_SHOWN -> Conditions.isArmorBarShown();
            case TARGETED_HUD_SHOWN -> Conditions.isTargetedCrosshairHUDShown();
        };
    }
}