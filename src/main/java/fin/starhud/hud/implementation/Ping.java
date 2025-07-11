package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.PingSettings;
import fin.starhud.helper.Box;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PingMeasurer;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.MultiValueDebugSampleLogImpl;
import net.minecraft.world.World;

public class Ping extends AbstractHUD {

    private static final PingSettings PING_SETTINGS = Main.settings.pingSettings;

    private static final Identifier PING_TEXTURE = Identifier.of("starhud", "hud/ping.png");

    private static long LAST_PING_UPDATE = -1L;
    private static World LAST_WORLD = null;
    private static PingMeasurer cachedPingMeasurer;

    private static final int TEXTURE_WIDTH = 63;
    private static final int TEXTURE_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public Ping() {
        super(PING_SETTINGS.base);
    }

    @Override
    public boolean shouldRender() {
        return baseHUDSettings.shouldRender && !CLIENT.isInSingleplayer() && shouldRenderOnCondition();
    }

    @Override
    public Box renderHUD(DrawContext context) {
        MultiValueDebugSampleLogImpl pingLog = CLIENT.getDebugHud().getPingLog();

        // different world and server checking for PingMeasurer renewal.
        World currentWorld = CLIENT.world;
        if (currentWorld != LAST_WORLD) {
            cachedPingMeasurer = new PingMeasurer(CLIENT.getNetworkHandler(), pingLog);
            LAST_WORLD = currentWorld;
        }

        updatePingLog();

        int pingLogLen = pingLog.getLength();
        if (pingLogLen <= 0)
            return null;

        // get the latest updated ping through the last element.
        long currentPing = pingLog.get(pingLogLen - 1);
        String pingStr = currentPing + " ms";

        // 0, 150, 300, 450
        int step = Math.min((int) currentPing / 150, 3);
        int color = getPingColor(step) | 0xFF000000;

        RenderUtils.drawTextureHUD(context, PING_TEXTURE, x, y, 0.0F, step * 13, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT * 4, color);
        RenderUtils.drawTextHUD(context, pingStr, x + 19, y + 3, color, false);

        return new Box(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT, color);
    }

    public static int getPingColor(int step) {
        return switch (step) {
            case 0 -> PING_SETTINGS.pingColor.first;
            case 1 -> PING_SETTINGS.pingColor.second;
            case 2 -> PING_SETTINGS.pingColor.third;
            case 3 -> PING_SETTINGS.pingColor.fourth;
            default -> 0xFFFFFFFF;
        };
    }

    // update pingLog every n seconds. Because this is quite expensive.
    private static void updatePingLog() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - LAST_PING_UPDATE >= 1000 * PING_SETTINGS.updateInterval) {
            LAST_PING_UPDATE = currentTimeMillis;
            cachedPingMeasurer.ping();
        }
    }

    @Override
    public int getBaseHUDWidth() {
        return TEXTURE_WIDTH;
    }

    @Override
    public int getBaseHUDHeight() {
        return TEXTURE_HEIGHT;
    }
}
