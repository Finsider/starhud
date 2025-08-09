package fin.starhud.helper;

import fin.starhud.helper.condition.*;

import java.util.function.Function;
import java.util.function.IntSupplier;

public enum Condition {
    DEBUG_HUD_OPENED(
            DebugHUD::isShown,
            () -> 0, // you can't really tell how to get the width / height of them so no can do.
            () -> 0
    ),

    CHAT_HUD_OPENED(
            ChatHUD::isShown,
            ChatHUD::getWidth,
            ChatHUD::getHeight
    ),

    BOSSBAR_SHOWN(
            BossBarHUD::isShown,
            BossBarHUD::getWidth,
            BossBarHUD::getHeight
    ),

    SCOREBOARD_SHOWN(
            ScoreboardHUD::isShown,
            ScoreboardHUD::getWidth,
            ScoreboardHUD::getHeight
    ),

    PLAYER_LIST_SHOWN(
            PlayerListHUD::isShown,
            PlayerListHUD::getWidth,
            PlayerListHUD::getHeight
    ),

    POSITIVE_EFFECT_SHOWN(
            StatusEffectHUD::isPositiveShown,
            StatusEffectHUD::getPositiveWidth,
            StatusEffectHUD::getPositiveHeight
    ),

    NEGATIVE_EFFECT_SHOWN(
            StatusEffectHUD::isNegativeShown,
            StatusEffectHUD::getNegativeWidth,
            StatusEffectHUD::getNegativeHeight
    ),

    OFFHAND_SHOWN(
            OffHandHUD::isShown,
            OffHandHUD::getWidth,
            OffHandHUD::getHeight
    ),

    HEALTH_BAR_SHOWN(
            HealthBarHUD::isShown,
            HealthBarHUD::getWidth,
            HealthBarHUD::getHeight
    ),

    FOOD_BAR_SHOWN(
            FoodBarHUD::isShown,
            FoodBarHUD::getWidth,
            FoodBarHUD::getHeight
    ),

    EXPERIENCE_BAR_SHOWN(
            ExperienceBarHUD::isShown,
            ExperienceBarHUD::getWidth,
            ExperienceBarHUD::getHeight
    ),

    AIR_BUBBLE_BAR_SHOWN(
            AirBubbleBarHUD::isShown,
            AirBubbleBarHUD::getWidth,
            AirBubbleBarHUD::getHeight
    ),

    ARMOR_BAR_SHOWN(
            ArmorBarHUD::isShown,
            ArmorBarHUD::getWidth,
            ArmorBarHUD::getHeight
    ),

    OVERLAY_MESSAGE_SHOWN(
            HeldItemTooltip::isShown,
            HeldItemTooltip::getWidth,
            HeldItemTooltip::getHeight
    ),

    TARGETED_HUD_SHOWN(
            TargetedCrosshair::isShown,
            TargetedCrosshair::getWidth,
            TargetedCrosshair::getHeight
    ),

    IS_MOD_LOADED(
            Other::isModLoaded,
            () -> 0,
            () -> 0
    ),

    IS_ON_SERVER(
            Other::isOnServer,
            () -> 0,
            () -> 0
    ),

    IS_IN_OVERWORLD(
            Other::isInOverworld,
            () -> 0,
            () -> 0
    ),

    IS_IN_NETHER(
            Other::isInNether,
            () -> 0,
            () -> 0
    ),

    IS_IN_END(
            Other::isInEnd,
            () -> 0,
            () -> 0
    );

    private final Function<String, Boolean> shownCheck;
    private final IntSupplier widthSupplier;
    private final IntSupplier heightSupplier;

    Condition(Function<String, Boolean> shownCheck, IntSupplier widthSupplier, IntSupplier heightSupplier) {
        this.shownCheck = shownCheck;
        this.widthSupplier = widthSupplier;
        this.heightSupplier = heightSupplier;
    }

    public boolean isConditionMet(String arg) {
        return shownCheck.apply(arg);
    }

    public int getWidth(){
        return widthSupplier.getAsInt();
    }

    public int getHeight() {
        return heightSupplier.getAsInt();
    }
}