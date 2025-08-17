package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.PingSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PingMeasurer;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.MultiValueDebugSampleLogImpl;
import net.minecraft.world.World;

public class PingHUD extends AbstractHUD {

    private static final PingSettings SETTINGS = Main.settings.pingSettings;

    private static final Identifier PING_TEXTURE = Identifier.of("starhud", "hud/ping.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13 * 4;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static long LAST_PING_UPDATE = -1L;
    private static World LAST_WORLD = null;
    private static PingMeasurer cachedPingMeasurer;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public PingHUD() {
        super(SETTINGS.base);
    }

    @Override
    public String getName() {
        return "Ping HUD";
    }

    @Override
    public String getId() {
        return HUDId.PING.toString();
    }

    private String pingStr;
    private int strWidth;
    private int color;
    private int step;

    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        displayMode = getSettings().getDisplayMode();

        MultiValueDebugSampleLogImpl pingLog = CLIENT.getDebugHud().getPingLog();

        // different world and server checking for PingMeasurer renewal.
        World currentWorld = CLIENT.world;
        if (currentWorld != LAST_WORLD) {
            cachedPingMeasurer = new PingMeasurer(CLIENT.getNetworkHandler(), pingLog);
            LAST_WORLD = currentWorld;
        }

        // update pingLog every n seconds. Because this is quite expensive.
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - LAST_PING_UPDATE >= 1000 * SETTINGS.updateInterval) {
            LAST_PING_UPDATE = currentTimeMillis;
            cachedPingMeasurer.ping();

            // cache string calculations here since ping was just updated
            int pingLogLen = pingLog.getLength();
            if (pingLogLen > 0) {
                long currentPing = pingLog.get(pingLogLen - 1);
                pingStr = currentPing + SETTINGS.additionalString;
                strWidth = CLIENT.textRenderer.getWidth(pingStr) - 1;

                step = Math.min((int) currentPing / 150, 3);
            }
        }

        color = (SETTINGS.useDynamicColor ? AbstractDurabilityHUD.getItemBarColor(3 - step, 3) : SETTINGS.color) | 0xFF000000;
        int width = displayMode.calculateWidth(ICON_WIDTH, strWidth);
        setWidthHeightColor(width, ICON_HEIGHT, color);

        return pingStr != null;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {

        int w = getWidth();
        int h = getHeight();

        return RenderUtils.drawSmallHUD(
                context,
                pingStr,
                x, y,
                w, h,
                PING_TEXTURE,
                0.0F, ICON_HEIGHT * step,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                color,
                displayMode,
                drawBackground
        );
    }

}
