package fin.starhud;

import fin.starhud.mixin.accessor.AccessorBossBarHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.util.math.MathHelper;


public class Helper {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static final char[] superscripts = "⁰¹²³⁴⁵⁶⁷⁸⁹".toCharArray();
    private static final char[] subscripts = "₀₁₂₃₄₅₆₇₈₉".toCharArray();

    // only convert numbers.
    public static String toSuperscript(String str) {
        char[] chars = str.toCharArray();

        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char c = chars[i];

            if (c >= '0' && c <= '9')
                chars[i] = superscripts[c - '0'];
        }

        return String.valueOf(chars);
    }

    public static String toSubscript(String str) {
        char[] chars = str.toCharArray();

        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char c = chars[i];

            if (c >= '0' && c <= '9')
                chars[i] = subscripts[c - '0'];
        }

        return String.valueOf(chars);
    }

    public static int getStep(int curr, int max, int maxStep) {
        return MathHelper.clamp(Math.round((float) curr * maxStep / (float) max), 0, maxStep);
    }

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
}