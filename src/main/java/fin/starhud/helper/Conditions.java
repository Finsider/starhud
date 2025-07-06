package fin.starhud.helper;

import fin.starhud.hud.implementation.TargetedCrosshair;
import fin.starhud.mixin.accessor.AccessorBossBarHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;

public class Conditions {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isChatFocused() {
        return CLIENT.inGameHud.getChatHud().isChatFocused();
    }

    public static boolean isDebugHUDOpen() {
        return CLIENT.getDebugHud().shouldShowDebugHud();
    }

    public static boolean isBossBarShown() {
        return !((AccessorBossBarHud) CLIENT.inGameHud.getBossBarHud()).getBossBars().isEmpty();
    }

    public static boolean isScoreBoardShown() {
        return CLIENT.world.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR) != null;
    }

    public static boolean isBeneficialEffectOverlayShown() {
        return CLIENT.player.getStatusEffects().stream()
                .anyMatch(effect -> effect.getEffectType().value().isBeneficial());
    }

    public static boolean isHarmEffectOverlayShown() {
        return CLIENT.player.getStatusEffects().stream()
                .anyMatch(effect -> !effect.getEffectType().value().isBeneficial());
    }

    public static boolean isOffHandOverlayShown() {
        return !CLIENT.player.getEquippedStack(EquipmentSlot.OFFHAND).isEmpty();
    }

    public static boolean isHealthBarShown() {
        return CLIENT.interactionManager.hasStatusBars();
    }

    public static boolean isExperienceBarShown() {
        return CLIENT.interactionManager.hasExperienceBar();
    }

    public static boolean isArmorBarShown() {
        return isHealthBarShown() && CLIENT.player.getArmor() > 0;
    }

    public static boolean isAirBubbleBarShown() {
        return isHealthBarShown() && CLIENT.player.isSubmergedIn(FluidTags.WATER) || CLIENT.player.getAir() < CLIENT.player.getMaxAir();
    }

    public static boolean isTargetedCrosshairHUDShown() {
        return TargetedCrosshair.shouldHUDRender();
    }
}
