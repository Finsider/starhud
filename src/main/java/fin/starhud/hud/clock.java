package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

import java.text.SimpleDateFormat;
import java.util.Date;

public class clock {

    private static Settings.ClockSettings.ClockInGameSettings clock_ingame = Main.settings.clockSettings.inGameSettings;

    private static final Identifier CLOCK_12 = Identifier.of("starhud", "hud/clock_12.png");
    private static final Identifier CLOCK_24 = Identifier.of("starhud", "hud/clock_24.png");

    private static final int height = 13;

    private static String minecraftTimeStr = "";
    private static int cachedMinecraftMinute = -1;

    private static boolean LAST_UPDATED_use12Hour_ingame = clock_ingame.use12Hour;

    private static int width_ingame = LAST_UPDATED_use12Hour_ingame ? 65 : 49;
    private static Identifier texture_ingame = LAST_UPDATED_use12Hour_ingame ? CLOCK_12 : CLOCK_24;

    public static void renderInGameTimeHUD(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;

        long time = world.getTimeOfDay() % 24000;

        boolean use12Hour = clock_ingame.use12Hour;

        int minutes = (int) ((time % 1000) * 3 / 50);
        int hours = (int) ((time / 1000) + 6) % 24;
        if (minutes != cachedMinecraftMinute) {
            cachedMinecraftMinute = minutes;

            minecraftTimeStr = use12Hour ?
                    buildMinecraftCivilianTimeString(hours, minutes):
                    buildMinecraftMilitaryTimeString(hours, minutes);
        }

        if (LAST_UPDATED_use12Hour_ingame != use12Hour) {
            LAST_UPDATED_use12Hour_ingame = use12Hour;
            modifyInGameClockVariables(hours, minutes);
        }

        int x = Helper.defaultHUDAlignmentX(clock_ingame.originX, context.getScaledWindowWidth(), width_ingame) + clock_ingame.x;
        int y = Helper.defaultHUDAlignmentY(clock_ingame.originY, context.getScaledWindowHeight(), height) + clock_ingame.y;

        int icon = getWeatherOrTime(world, time);
        int color = getIconColor(icon) | 0xFF000000;

        Helper.drawTextureAlphaColor(context, texture_ingame, x, y, 0.0F, icon * 13, width_ingame, height, width_ingame, height * 5, color);
        context.drawText(client.textRenderer, minecraftTimeStr, x + 19, y + 3, color, false);
    }

    private static int getIconColor(int icon) {
        return switch (icon) {
            case 1 -> clock_ingame.color.day;
            case 2 -> clock_ingame.color.night;
            case 3 -> clock_ingame.color.rain;
            case 4 -> clock_ingame.color.thunder;
            default -> 0xFFFFFF;
        };
    }

    private static int getWeatherOrTime(ClientWorld clientWorld, long time) {
        if (clientWorld.isThundering()) return 4;
        else if (clientWorld.isRaining()) return 3;
        else if ((12542 < time && time < 23460) || clientWorld.isNight()) return 2;
        else return 1;
    }

    private static String buildMinecraftMilitaryTimeString(int hours, int minutes) {
        StringBuilder timeBuilder = new StringBuilder();

        if (hours < 10) timeBuilder.append('0');
        timeBuilder.append(hours).append(':');

        if (minutes < 10) timeBuilder.append('0');
        timeBuilder.append(minutes);

        return timeBuilder.toString();
    }

    private static String buildMinecraftCivilianTimeString(int hours, int minutes) {
        StringBuilder timeBuilder = new StringBuilder();

        String period = hours >= 12 ? " PM" : " AM";

        // 01.00 until 12.59 AM / PM
        hours %= 12;
        if (hours == 0) hours = 12;

        timeBuilder.append(buildMinecraftMilitaryTimeString(hours, minutes)).append(period);

        return timeBuilder.toString();
    }

    private static void modifyInGameClockVariables(int hours, int minutes) {
        if (LAST_UPDATED_use12Hour_ingame) {
            width_ingame = 65;
            texture_ingame = CLOCK_12;
            minecraftTimeStr = buildMinecraftCivilianTimeString(hours, minutes);
        } else {
            width_ingame = 49;
            texture_ingame = CLOCK_24;
            minecraftTimeStr = buildMinecraftMilitaryTimeString(hours, minutes);
        }
    }

    private static Settings.ClockSettings.ClockSystemSettings clock_system = Main.settings.clockSettings.systemSettings;

    private static final SimpleDateFormat militaryTimeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat civilianTimeFormat = new SimpleDateFormat("hh:mm a");

    private static String systemTimeStr = buildSystemMilitaryTimeString(System.currentTimeMillis());
    private static long cachedSystemMinute = -1;

    private static Boolean LAST_UPDATED_use12Hour_system = clock_system.use12Hour;

    private static int width_system = LAST_UPDATED_use12Hour_system ? 65 : 49;
    private static Identifier texture_system = LAST_UPDATED_use12Hour_system ? CLOCK_12 : CLOCK_24;

    public static void renderSystemTimeHUD(DrawContext context) {
        if (!clock_system.shouldRender) return;

        MinecraftClient client = MinecraftClient.getInstance();

        boolean use12Hour = clock_system.use12Hour;

        // update each minute
        long currentTime = System.currentTimeMillis();
        long minute = currentTime / 60000;
        if (minute != cachedSystemMinute) {
            cachedSystemMinute = minute;
            systemTimeStr = use12Hour ?
                    buildSystemCivilianTimeString(currentTime):
                    buildSystemMilitaryTimeString(currentTime);
        }

        if (LAST_UPDATED_use12Hour_system != use12Hour) {
            LAST_UPDATED_use12Hour_system = use12Hour;
            modifySystemClockVariables(currentTime);
        }

        int x = Helper.defaultHUDAlignmentX(clock_system.originX, context.getScaledWindowWidth(), width_system) + clock_system.x;
        int y = Helper.defaultHUDAlignmentY(clock_system.originY, context.getScaledWindowHeight(), height) + clock_system.y;
        int color = clock_system.color | 0xFF000000;

        Helper.drawTextureAlphaColor(context, texture_system, x, y, 0.0F, 0.0F, width_system, height, width_system, height * 5, color);
        context.drawText(client.textRenderer, systemTimeStr, x + 19, y + 3, color, false);
    }

    private static String buildSystemMilitaryTimeString(long time) {
        return militaryTimeFormat.format(new Date(time));
    }
    private static String buildSystemCivilianTimeString(long time) {
        return civilianTimeFormat.format(new Date(time));
    }

    private static void modifySystemClockVariables(long time) {
        if (LAST_UPDATED_use12Hour_system) {
            width_system = 65;
            texture_system = CLOCK_12;
            systemTimeStr = buildSystemCivilianTimeString(time);
        } else {
            width_system = 49;
            texture_system = CLOCK_24;
            systemTimeStr = buildSystemMilitaryTimeString(time);
        }
    }
}