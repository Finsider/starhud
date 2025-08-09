package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.PingSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class PingHUD extends AbstractHUD {

    private static final PingSettings PING_SETTINGS = Main.settings.pingSettings;

    private static final Identifier PING_TEXTURE = Identifier.of("starhud", "hud/ping.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13 * 4;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public PingHUD() {
        super(PING_SETTINGS.base);
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
    private int width;
    private int height;
    private int color;
    private int step;

    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        displayMode = getSettings().getDisplayMode();

        PlayerListEntry playerListEntry = CLIENT.getNetworkHandler().getPlayerListEntry(CLIENT.player.getUuid());

        int currentPing = playerListEntry.getLatency();

        pingStr = currentPing + " ms";
        strWidth = CLIENT.textRenderer.getWidth(pingStr) - 1;

        step = Math.min(currentPing / 150, 3);

        color = getPingColor(step) | 0xFF000000;
        width = displayMode.calculateWidth(ICON_WIDTH, strWidth);
        height = ICON_HEIGHT;
        setWidthHeightColor(width, height, color);

        return pingStr != null;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {

        int w = getWidth();
        int h = getHeight();

        RenderUtils.drawSmallHUD(
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

        return true;
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

}
