package fin.starhud.hud.implementation.other;

import fin.starhud.Main;
import fin.starhud.config.hud.ReachSettings;
import fin.starhud.helper.AttackTracker;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ReachHUD extends AbstractHUD {

    private static final ReachSettings SETTINGS = Main.settings.reachSettings;

    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/reach.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public ReachHUD() {
        super(SETTINGS.base);
    }

    private String str;
    private int color;
    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {

        float reach = (float) Math.round(AttackTracker.getReach() * 100) / 100;

        if (reach == -1) {
            if (SETTINGS.hideInactive)
                return false;
            else
                reach = 0;
        }

        str = reach + SETTINGS.additionalString;
        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        displayMode = getSettings().getDisplayMode();

        int width = displayMode.calculateWidth(ICON_WIDTH, strWidth);

        color = SETTINGS.color | 0xFF000000;

        setWidthHeightColor(width, TEXTURE_HEIGHT, color);

        return str != null;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {

        int w = getWidth();
        int h = getHeight();

        return RenderUtils.drawSmallHUD(
                context,
                str,
                x, y,
                w, h,
                TEXTURE,
                0.0F, 0.0F,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                color,
                displayMode,
                drawBackground
        );
    }

    @Override
    public String getName() {
        return "Reach HUD";
    }

    @Override
    public String getId() {
        return HUDId.REACH.toString();
    }
}
