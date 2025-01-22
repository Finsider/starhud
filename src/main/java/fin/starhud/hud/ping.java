package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PingMeasurer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.MultiValueDebugSampleLogImpl;
import net.minecraft.world.World;

public class ping {

    private static final Settings.PingSettings ping = Main.settings.pingSettings;

    private static final Identifier PING_TEXTURE = Identifier.of("starhud", "hud/ping.png");

    private static long LAST_PING_UPDATE = -1L;
    private static PingMeasurer pingMeasurer;
    private static World lastWorld = null;

    private static final int width = 63;
    private static final int height = 13;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void renderPingHUD(DrawContext context) {
        if ((ping.hideOn.f3 && Helper.isDebugHUDOpen()) || (ping.hideOn.chat && Helper.isChatFocused())) return;
        if (client.isInSingleplayer()) return;

        MultiValueDebugSampleLogImpl pingLog = client.getDebugHud().getPingLog();

        // different world and server checking for PingMeasurer renewal.
        World current_world = client.world;
        if (current_world != lastWorld) {
            pingMeasurer = new PingMeasurer(client.getNetworkHandler(), pingLog);
            lastWorld = current_world;
        }

        updatePingLog();

        int pingLogLen = pingLog.getLength();
        if (pingLogLen <= 0) return;

        // get the latest updated ping through the last element.
        long currentPing = pingLog.get(pingLogLen - 1);
        String pingStr = currentPing + " ms";

        int x = Helper.calculatePositionX(ping.x, ping.originX, width, ping.scale);
        int y = Helper.calculatePositionY(ping.y, ping.originY, height, ping.scale);

        // 0, 150, 300, 450
        int step = Math.min((int) currentPing / 150, 3);
        int color = getPingColor(step) | 0xFF000000;

        context.getMatrices().push();
        Helper.setHUDScale(context, ping.scale);

        context.drawTexture(RenderLayer::getGuiTextured, PING_TEXTURE, x, y, 0.0F, step * 13, width, height, width, height * 4, color);
        context.drawText(client.textRenderer, pingStr, x + 19, y + 3, color, false);

        context.getMatrices().pop();
    }

    public static int getPingColor(int step) {
        return switch (step) {
            case 0 -> ping.pingColor.first;
            case 1 -> ping.pingColor.second;
            case 2 -> ping.pingColor.third;
            case 3 -> ping.pingColor.fourth;
            default -> 0xFFFFFFFF;
        };
    }

    // update pingLog every n seconds. Because this is quite expensive.
    private static void updatePingLog() {
        long current_time = System.currentTimeMillis();
        if (current_time - LAST_PING_UPDATE >= 1000 * ping.updateInterval) {
            LAST_PING_UPDATE = current_time;
            pingMeasurer.ping();
        }
    }
}
