package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.ClockInGameSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

public class ClockInGameHUD extends AbstractHUD {

    private static final ClockInGameSettings CLOCK_IN_GAME_SETTINGS = Main.settings.clockSettings.inGameSetting;

    private static final Identifier CLOCK_IN_GAME_TEXTURE = Identifier.of("starhud", "hud/clock_ingame.png");

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13 * 4;

    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private static String cachedMinecraftTimeString = "";
    private static int cachedMinecraftMinute = -1;
    private static int cachedMinecraftHours = -1;
    private static int cachedStrWidth = -1;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public ClockInGameHUD() {
        super(CLOCK_IN_GAME_SETTINGS.base);
    }

    @Override
    public String getName() {
        return "Clock In-Game HUD";
    }

    @Override
    public String getId() {
        return HUDId.CLOCK_INGAME.toString();
    }

    private int width;
    private int height;
    private int color;
    private int iconIndex;

    private HUDDisplayMode displayMode;

    @Override
    public boolean collectHUDInformation() {
        ClientWorld world = CLIENT.world;

        long time = world.getTimeOfDay() % 24000;

        boolean use12Hour = CLOCK_IN_GAME_SETTINGS.use12Hour;
        displayMode = getSettings().getDisplayMode();

        int minutes = (int) ((time % 1000) * 3 / 50);
        int hours = (int) ((time / 1000) + 6) % 24;
        if (minutes != cachedMinecraftMinute || hours != cachedMinecraftHours) {
            cachedMinecraftMinute = minutes;
            cachedMinecraftHours = hours;

            cachedMinecraftTimeString = use12Hour ?
                    buildMinecraftTime12String(hours, minutes) :
                    buildMinecraftTime24String(hours, minutes);

            cachedStrWidth = CLIENT.textRenderer.getWidth(cachedMinecraftTimeString) - 1;
        }

        iconIndex = getWeatherOrTime(world);
        color = getIconColor(iconIndex) | 0xFF000000;

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
                cachedMinecraftTimeString,
                x, y,
                w, h,
                CLOCK_IN_GAME_TEXTURE,
                0.0F, ICON_HEIGHT * iconIndex,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                color,
                displayMode,
                drawBackground
        );

        return true;
    }

    private static int getIconColor(int icon) {
        return switch (icon) {
            case 0 -> CLOCK_IN_GAME_SETTINGS.color.day;
            case 1 -> CLOCK_IN_GAME_SETTINGS.color.night;
            case 2 -> CLOCK_IN_GAME_SETTINGS.color.rain;
            case 3 -> CLOCK_IN_GAME_SETTINGS.color.thunder;
            default -> 0xFFFFFF;
        };
    }

    private static int getWeatherOrTime(ClientWorld clientWorld) {
        if (clientWorld.isThundering()) return 3;
        else if (clientWorld.isRaining()) return 2;
        else if (clientWorld.isNight()) return 1;
        else return 0;
    }

    private static String buildMinecraftTime24String(int hours, int minutes) {
        StringBuilder timeBuilder = new StringBuilder();

        if (hours < 10) timeBuilder.append('0');
        timeBuilder.append(hours).append(':');

        if (minutes < 10) timeBuilder.append('0');
        timeBuilder.append(minutes);

        return timeBuilder.toString();
    }

    private static String buildMinecraftTime12String(int hours, int minutes) {
        StringBuilder timeBuilder = new StringBuilder();

        String period = hours >= 12 ? " PM" : " AM";

        // 01.00 until 12.59 AM / PM
        hours %= 12;
        if (hours == 0) hours = 12;

        timeBuilder.append(buildMinecraftTime24String(hours, minutes)).append(period);

        return timeBuilder.toString();
    }

    @Override
    public void update(){
        super.update();
        cachedMinecraftMinute = -1;
        cachedMinecraftHours = -1;
    }
}
