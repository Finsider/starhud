package fin.starhud.hud.implementation.other;

import fin.starhud.Main;
import fin.starhud.config.hud.PlayerCountSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Identifier;

import java.util.Collection;

public class PlayerCountHUD extends AbstractHUD  {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final PlayerCountSettings SETTINGS = Main.settings.playerCountSettings;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/player_count.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private String str;
    private HUDDisplayMode displayMode;

    public PlayerCountHUD() {
        super(SETTINGS.base);
    }

    @Override
    public boolean collectHUDInformation() {
        if (CLIENT.player == null) return false;

        int currentPlayer = -1, maxPlayer = -1;
        if (CLIENT.isInSingleplayer()) {
            IntegratedServer server = CLIENT.getServer();
            if (server == null) return false;

            currentPlayer = server.getCurrentPlayerCount();
            maxPlayer = server.getMaxPlayerCount();

        } else {
            Collection<PlayerListEntry> playerListEntries = CLIENT.player.networkHandler.getListedPlayerListEntries();
            currentPlayer = playerListEntries.size();

            if (SETTINGS.showMaxPlayer) {
                ServerInfo serverInfo = CLIENT.getCurrentServerEntry();
                if (serverInfo != null && serverInfo.players != null)
                    maxPlayer = serverInfo.players.max();
            }
        }

        if (currentPlayer < 0) return false;

        str = Integer.toString(currentPlayer);
        if (SETTINGS.showMaxPlayer && maxPlayer > 0)
            str += "/" + maxPlayer;

        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        displayMode = getSettings().getDisplayMode();
        int width = displayMode.calculateWidth(ICON_WIDTH, strWidth);
        int height = TEXTURE_HEIGHT;
        int color = SETTINGS.color | 0xFF000000;

        setWidthHeightColor(width, height, color);

        return str != null;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {

        int w = getWidth(), h = getHeight(), c = getColor();

        return RenderUtils.drawSmallHUD(
                context,
                str,
                x, y,
                w, h,
                TEXTURE,
                0.0F, 0.0F,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                c,
                displayMode,
                drawBackground
        );
    }

    @Override
    public String getName() {
        return "Player Count HUD";
    }

    @Override
    public String getId() {
        return HUDId.PLAYER_COUNT.toString();
    }
}
