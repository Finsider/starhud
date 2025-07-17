package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.ClockInGameSettings;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

public class ClockInGameHUD extends AbstractHUD {

    private static final ClockInGameSettings CLOCK_IN_GAME_SETTINGS = Main.settings.clockSettings.inGameSetting;

    private static final Identifier CLOCK_12_TEXTURE = Identifier.of("starhud", "hud/clock_12.png");
    private static final Identifier CLOCK_24_TEXTURE = Identifier.of("starhud", "hud/clock_24.png");

    private static final int TEXTURE_HEIGHT = 13;

    private static String cachedMinecraftTimeString = "";
    private static int cachedMinecraftMinute = -1;
    private static boolean cachedInGameUse12Hour = CLOCK_IN_GAME_SETTINGS.use12Hour;

    private static final int TEXTURE_INGAME_12_WIDTH = 65;
    private static final int TEXTURE_INGAME_24_WIDTH = 49;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public ClockInGameHUD() {
        super(CLOCK_IN_GAME_SETTINGS.base);
    }

    @Override
    public String getName() {
        return "Clock In-Game HUD";
    }

    @Override
    public boolean renderHUD(DrawContext context) {
        ClientWorld world = CLIENT.world;

        long time = world.getTimeOfDay() % 24000;

        boolean use12Hour = CLOCK_IN_GAME_SETTINGS.use12Hour;

        int minutes = (int) ((time % 1000) * 3 / 50);
        int hours = (int) ((time / 1000) + 6) % 24;
        if (minutes != cachedMinecraftMinute || use12Hour != cachedInGameUse12Hour) {
            cachedMinecraftMinute = minutes;
            cachedInGameUse12Hour = use12Hour;

            cachedMinecraftTimeString = use12Hour ?
                    buildMinecraftTime12String(hours, minutes) :
                    buildMinecraftTime24String(hours, minutes);
        }

        int icon = getWeatherOrTime(world);
        int color = getIconColor(icon) | 0xFF000000;

        if (use12Hour) {
            RenderUtils.drawTextureHUD(context, CLOCK_12_TEXTURE, x, y, 0.0F, icon * 13, TEXTURE_INGAME_12_WIDTH, TEXTURE_HEIGHT, TEXTURE_INGAME_12_WIDTH, TEXTURE_HEIGHT * 5, color);
            RenderUtils.drawTextHUD(context, cachedMinecraftTimeString, x + 19, y + 3, color, false);;
            setBoundingBox(x, y, TEXTURE_INGAME_12_WIDTH, TEXTURE_HEIGHT, color);
        } else {
            RenderUtils.drawTextureHUD(context, CLOCK_24_TEXTURE, x, y, 0.0F, icon * 13, TEXTURE_INGAME_24_WIDTH, TEXTURE_HEIGHT, TEXTURE_INGAME_24_WIDTH, TEXTURE_HEIGHT * 5, color);
            RenderUtils.drawTextHUD(context, cachedMinecraftTimeString, x + 19, y + 3, color, false);
            setBoundingBox(x, y, TEXTURE_INGAME_24_WIDTH, TEXTURE_HEIGHT, color);
        }
        return true;
    }

    private static int getIconColor(int icon) {
        return switch (icon) {
            case 1 -> CLOCK_IN_GAME_SETTINGS.color.day;
            case 2 -> CLOCK_IN_GAME_SETTINGS.color.night;
            case 3 -> CLOCK_IN_GAME_SETTINGS.color.rain;
            case 4 -> CLOCK_IN_GAME_SETTINGS.color.thunder;
            default -> 0xFFFFFF;
        };
    }

    private static int getWeatherOrTime(ClientWorld clientWorld) {
        if (clientWorld.isThundering()) return 4;
        else if (clientWorld.isRaining()) return 3;
        else if (clientWorld.isNight()) return 2;
        else return 1;
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
    public int getBaseHUDWidth() {
        return CLOCK_IN_GAME_SETTINGS.use12Hour ? TEXTURE_INGAME_12_WIDTH : TEXTURE_INGAME_24_WIDTH;
    }

    @Override
    public int getBaseHUDHeight() {
        return TEXTURE_HEIGHT;
    }
}
