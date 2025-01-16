package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

public class ping {

    public static Settings.PingSettings ping = Main.settings.pingSettings;

    private static final Identifier PING_TEXTURE = Identifier.of("starhud", "hud/ping.png");

    private static final int width = 63;
    private static final int height = 13;

    public static void renderPingHUD(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.isInSingleplayer()) return;

        ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
        if (networkHandler == null) return;

        PlayerListEntry playerListEntry = networkHandler.getPlayerListEntry(client.player.getUuid());
        if (playerListEntry == null) return;

        int currentPing = playerListEntry.getLatency();

        int x = Helper.defaultHUDAlignmentX(ping.originX, context.getScaledWindowWidth(), width) + ping.x;
        int y = Helper.defaultHUDAlignmentY(ping.originY, context.getScaledWindowHeight(), height) + ping.y;

        // 0, 150, 300, 450
        int step = Math.min(currentPing / 150, 3);
        int color = getPingColor(step) | 0xFF000000;

        Helper.drawTextureAlphaColor(context, PING_TEXTURE, x, y, 0.0F, step * 13, width, height, width, height * 4, color);
        context.drawText(client.textRenderer, currentPing + " ms", x + 19, y + 3, color, false);
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
}