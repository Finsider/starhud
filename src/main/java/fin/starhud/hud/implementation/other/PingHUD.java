package fin.starhud.hud.implementation.other;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.hud.other.PingSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

public class PingHUD extends AbstractHUD {

    private static final PingSettings SETTINGS = Main.settings.pingSettings;

    private static final Identifier PING_TEXTURE = Identifier.of("starhud", "hud/ping.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13 * 4;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

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

        ClientPlayNetworkHandler networkHandler = CLIENT.getNetworkHandler();

        if (networkHandler == null)
            return false;

        PlayerListEntry playerListEntry = networkHandler.getPlayerListEntry(CLIENT.player.getUuid());

        if (playerListEntry == null)
            return false;

        int currentPing = playerListEntry.getLatency();

        pingStr = currentPing + " ms";
        strWidth = CLIENT.textRenderer.getWidth(pingStr) - 1;

        step = Math.min(currentPing / 150, 3);

        color = (SETTINGS.useDynamicColor ? Helper.getItemBarColor(3 - step, 3) : SETTINGS.color) | 0xFF000000;
        int width = displayMode.calculateWidth(ICON_WIDTH, strWidth);
        int height = ICON_HEIGHT;
        setWidthHeightColor(width, height, color);

        return pingStr != null;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground, boolean drawTextShadow) {

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
                drawBackground,
                drawTextShadow
        );
    }

}
