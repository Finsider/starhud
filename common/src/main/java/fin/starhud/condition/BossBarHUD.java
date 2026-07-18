package fin.starhud.condition;

import fin.starhud.mixin.accessor.AccessorBossBarHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.LerpingBossEvent;

import java.util.Collection;

public class BossBarHUD {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    private static int cachedVisibleBossBarCount = -1;
    private static int cachedBossBarAmount;
    private static int cachedScreenHeight = -1;

    public static boolean isShown(String ignored) {
        return !((AccessorBossBarHud) CLIENT.gui.getBossOverlay()).getBossBars().isEmpty();
    }

    public static int getWidth() {
        return 182;
    }

    public static int getHeight() {
        return (getVisibleBossBarCount() * 19) - 2;
    }

    public static int getVisibleBossBarCount() {
        Collection<LerpingBossEvent> bars = ((AccessorBossBarHud) CLIENT.gui.getBossOverlay()).getBossBars().values();
        int currentBossBarAmount = bars.size();
        int currentScreenHeight = CLIENT.getWindow().getGuiScaledHeight();

        if (cachedBossBarAmount == currentBossBarAmount && cachedScreenHeight == currentScreenHeight) {
            return cachedVisibleBossBarCount;
        }

        cachedBossBarAmount = currentBossBarAmount;
        cachedScreenHeight = currentScreenHeight;

        int maxY = currentScreenHeight / 3;

        int j = 12;
        int count = 0;

        for (LerpingBossEvent ignored : bars) {
            count++;
            j += 19;
            if (j >= maxY) break;
        }

        cachedVisibleBossBarCount = count;
        return cachedVisibleBossBarCount;
    }
}
