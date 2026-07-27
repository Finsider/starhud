package fin.starhud.hud.implementation.other;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.hud.other.PingSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;

public class PingHUD extends AbstractHUD {

    private static final PingSettings SETTINGS = Main.settings.pingSettings;

    private static final ResourceLocation PING_TEXTURE = ResourceLocation.tryBuild("starhud", "hud/ping.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13 * 4;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static final Minecraft CLIENT = Minecraft.getInstance();

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

        ClientPacketListener networkHandler = CLIENT.getConnection();

        if (networkHandler == null) return false;

        PlayerInfo playerListEntry = networkHandler.getPlayerInfo(CLIENT.player.getUUID());

        if (playerListEntry == null) return false;

        int currentPing = playerListEntry.getLatency();

        pingStr = currentPing + SETTINGS.additionalString;
        strWidth = CLIENT.font.width(pingStr) - 1;

        step = Math.min(currentPing / 150, 3);

        color = (SETTINGS.useDynamicColor ? Helper.getItemBarColor(3 - step, 3) : SETTINGS.color) | 0xFF000000;
        int width = displayMode.calculateWidth(ICON_WIDTH, strWidth);
        setWidthHeightColor(width, ICON_HEIGHT, color);

        return pingStr != null;
    }

    @Override
    public boolean renderHUD(GuiGraphics context, int x, int y, boolean drawBackground, boolean drawTextShadow) {

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
