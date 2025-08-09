package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.ClockSystemSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockSystemHUD extends AbstractHUD {

    private static final ClockSystemSettings CLOCK_SYSTEM_SETTINGS = Main.settings.clockSettings.systemSetting;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static final Identifier CLOCK_SYSTEM_TEXTURE = Identifier.of("starhud", "hud/clock_system.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static final SimpleDateFormat CLOCK_24_FORMAT = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat CLOCK_12_FORMAT = new SimpleDateFormat("hh:mm a");

    private static String cachedSystemTimeString = buildSystemTime24String(System.currentTimeMillis());
    private static long cachedSystemMinute = -1;
    private static int cachedStrWidth = -1;

    public ClockSystemHUD() {
        super(CLOCK_SYSTEM_SETTINGS.base);
    }

    @Override
    public String getName() {
        return "Clock System HUD";
    }

    @Override
    public String getId() {
        return HUDId.CLOCK_SYSTEM.toString();
    }

    private int width;
    private int height;
    private int color;

    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        // update each minute
        long currentTime = System.currentTimeMillis();
        long minute = currentTime / 60000;

        boolean use12Hour = CLOCK_SYSTEM_SETTINGS.use12Hour;

        // update on either a new minute or user updated the config
        if (minute != cachedSystemMinute) {
            cachedSystemMinute = minute;

            cachedSystemTimeString = use12Hour ?
                    buildSystemTime12String(currentTime).toUpperCase() :
                    buildSystemTime24String(currentTime);

            cachedStrWidth = CLIENT.textRenderer.getWidth(cachedSystemTimeString) - 1;
        }

        displayMode = getSettings().getDisplayMode();
        color = CLOCK_SYSTEM_SETTINGS.color | 0xFF000000;

        width = displayMode.calculateWidth(ICON_WIDTH, cachedStrWidth);
        height = ICON_HEIGHT;

        setWidthHeightColor(width, height, color);

        return true;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground) {

        int w = getWidth();
        int h = getHeight();

        RenderUtils.drawSmallHUD(
                context,
                cachedSystemTimeString,
                x, y,
                w, h,
                CLOCK_SYSTEM_TEXTURE,
                0.0F, 0.0F,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                color,
                displayMode,
                drawBackground
        );

        return true;
    }

    private static String buildSystemTime24String(long time) {
        return CLOCK_24_FORMAT.format(new Date(time));
    }

    private static String buildSystemTime12String(long time) {
        return CLOCK_12_FORMAT.format(new Date(time));
    }

    @Override
    public void update() {
        super.update();
        cachedSystemMinute = -1;
    }
}
