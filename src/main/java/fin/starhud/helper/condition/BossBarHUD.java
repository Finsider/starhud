package fin.starhud.helper.condition;

import fin.starhud.mixin.accessor.AccessorBossBarHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ClientBossBar;

import java.util.Collection;

public class BossBarHUD {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static int cachedVisibleBossBarCount = -1;
    private static int cachedBossBarAmount;
    private static int cachedScreenHeight = -1;

    public static boolean isShown(String ignored) {
        return !((AccessorBossBarHud) CLIENT.inGameHud.getBossBarHud()).getBossBars().isEmpty();
    }

    public static int getWidth() {
        return 182;
    }

    public static int getHeight() {
        return (getVisibleBossBarCount() * 19) - 2;
    }

    public static int getVisibleBossBarCount() {
        Collection<ClientBossBar> bars = ((AccessorBossBarHud) CLIENT.inGameHud.getBossBarHud()).getBossBars().values();
        int currentBossBarAmount = bars.size();
        int currentScreenHeight = CLIENT.getWindow().getScaledHeight();

        if (cachedBossBarAmount == currentBossBarAmount && cachedScreenHeight == currentScreenHeight) {
            return cachedVisibleBossBarCount;
        }

        cachedBossBarAmount = currentBossBarAmount;
        cachedScreenHeight = currentScreenHeight;

        int maxY = currentScreenHeight / 3;

        int j = 12;
        int count = 0;

        for (ClientBossBar ignored : bars) {
            count++;
            j += 19;
            if (j >= maxY) break;
        }

        cachedVisibleBossBarCount = count;
        return cachedVisibleBossBarCount;
    }
}
