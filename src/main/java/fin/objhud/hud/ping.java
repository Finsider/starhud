package fin.objhud.hud;

import fin.objhud.Helper;
import fin.objhud.Main;
import fin.objhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PingMeasurer;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.MultiValueDebugSampleLogImpl;
import net.minecraft.world.World;

public class ping {

    public static Settings.PingSettings ping = Main.settings.pingSettings;

    private static final Identifier PING_TEXTURE = Identifier.of("objhud", "hud/ping.png");

    private static long LAST_PING_UPDATE = -1L;
    private static PingMeasurer pingMeasurer;
    private static World lastWorld = null;

    public static void renderPingHUD(DrawContext context) {
        if (!ping.renderPingHUD) return;

        MinecraftClient client = MinecraftClient.getInstance();
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
        String pingStr = Long.toString(currentPing);

        int x = Helper.defaultHUDLocationX(ping.defX, context) + ping.x;
        int y = Helper.defaultHUDLocationY(ping.defY, context) + ping.y;

        // 0, 150, 300, 450
        int step = Math.min((int) currentPing / 150, 3);
        int color = getPingColor(step) | 0xFF000000;

        Helper.drawTextureAlphaColor(context, PING_TEXTURE, x, y, 0.0F, step * 13, 47, 13, 47, 52, color);
        context.drawText(client.textRenderer, pingStr, x + 19, y + 3, color, false);
    }

    public static int getPingColor(int step) {
        return switch (step) {
            case 0 -> ping.color.first;
            case 1 -> ping.color.second;
            case 2 -> ping.color.third;
            case 3 -> ping.color.fourth;
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
