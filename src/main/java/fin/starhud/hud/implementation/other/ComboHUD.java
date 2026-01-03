package fin.starhud.hud.implementation.other;

import fin.starhud.Main;
import fin.starhud.config.hud.other.ComboSettings;
import fin.starhud.helper.AttackTracker;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ComboHUD extends AbstractHUD {

    private static final ComboSettings SETTINGS = Main.settings.comboSettings;

    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/combo.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public ComboHUD() {
        super(SETTINGS.base);
    }

    private String str;
    private int color;
    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        long combo = AttackTracker.getCombo();

        if (combo == -1) {
            if (SETTINGS.hideInactive)
                return false;
            else
                combo = 0;
        }

        str = combo + SETTINGS.additionalString;
        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        displayMode = getSettings().getDisplayMode();

        int width = displayMode.calculateWidth(ICON_WIDTH, strWidth);

        color = SETTINGS.color | 0xFF000000;

        setWidthHeightColor(width, TEXTURE_HEIGHT, color);

        return str != null;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground, boolean drawTextShadow) {

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
                drawBackground,
                drawTextShadow
        );
    }

    @Override
    public String getName() {
        return "Combo HUD";
    }

    @Override
    public String getId() {
        return HUDId.COMBO.toString();
    }
}
