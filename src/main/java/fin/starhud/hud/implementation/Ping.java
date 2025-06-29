package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.PingSettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

public class Ping extends AbstractHUD {

    private static final PingSettings PING_SETTINGS = Main.settings.pingSettings;

    private static final Identifier PING_TEXTURE = Identifier.of("starhud", "hud/ping.png");

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
    public void renderHUD(DrawContext context) {

        ClientPlayNetworkHandler networkHandler = CLIENT.getNetworkHandler();
        if (networkHandler == null) return;

        PlayerListEntry playerListEntry = networkHandler.getPlayerListEntry(CLIENT.player.getUuid());
        if (playerListEntry == null) return;

        int currentPing = playerListEntry.getLatency();
        String pingStr = currentPing + " ms";

        // 0, 150, 300, 450
        int step = Math.min(currentPing / 150, 3);
        int color = getPingColor(step) | 0xFF000000;

        RenderUtils.drawTextureHUD(context, PING_TEXTURE, x, y, 0.0F, step * 13, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT * 4, color);
        RenderUtils.drawTextHUD(context, pingStr, x + 19, y + 3, color, false);
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

    @Override
    public int getBaseHUDWidth() {
        return TEXTURE_WIDTH;
    }

    @Override
    public int getBaseHUDHeight() {
        return TEXTURE_HEIGHT;
    }
}
