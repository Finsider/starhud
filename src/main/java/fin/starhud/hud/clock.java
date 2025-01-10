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

    private static Settings.ClockSettings.ClockSystemSettings clock_system = Main.settings.clockSettings.systemSettings;
    private static Settings.ClockSettings.ClockInGameSettings clock_ingame = Main.settings.clockSettings.inGameSettings;

    private static final Identifier CLOCK_SYSTEM = Identifier.of("starhud", "hud/clock_system.png");
    private static final Identifier CLOCK_INGAME = Identifier.of("starhud", "hud/clock_ingame.png");

    private static String minecraftTimeStr = "";
    private static int cachedMinecraftMinute = -1;

    public static void renderInGameTimeHUD(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientWorld world = mc.world;

        // update each tick
        long time = world.getTimeOfDay() % 24000;

        int minutes = (int) ((time % 1000) * 3 / 50);
        if (minutes != cachedMinecraftMinute) {
            cachedMinecraftMinute = minutes;
            int hours = (int) ((time / 1000) + 6) % 24;
            minecraftTimeStr = buildMinecraftTimeString(hours, minutes);
        }

        int width = 49;
        int height = 13;

        int x = Helper.defaultHUDLocationX(clock_ingame.originX, context, width) + clock_ingame.x;
        int y = Helper.defaultHUDLocationY(clock_ingame.originY, context, height) + clock_ingame.y;

        int icon = getWeatherOrTime(world);
        int color = getIconColor(icon) | 0xFF000000;

        context.drawTexture(RenderLayer::getGuiTextured, CLOCK_INGAME, x, y, 0.0F, icon * 13, width, height, width, height * 4, color);
        context.drawText(mc.textRenderer, minecraftTimeStr, x + 19, y + 3, color, false);
    }

    private static int getIconColor(int icon) {
        return switch (icon) {
            case 0 -> clock_ingame.color.day;
            case 1 -> clock_ingame.color.night;
            case 2 -> clock_ingame.color.rain;
            case 3 -> clock_ingame.color.thunder;
            default -> 0xFFFFFF;
        };
    }

    private static int getWeatherOrTime(ClientWorld clientWorld) {
        if (clientWorld.isThundering()) return 3;
        else if (clientWorld.isRaining()) return 2;
        else if (clientWorld.isNight()) return 1;
        else return 0;
    }

    private static String buildMinecraftTimeString(int hours, int minutes) {
        StringBuilder timeBuilder = new StringBuilder();

        if (hours < 10) timeBuilder.append('0');
        timeBuilder.append(hours).append(':');

        if (minutes < 10) timeBuilder.append('0');
        timeBuilder.append(minutes);

        return timeBuilder.toString();
    }

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static String systemTimeStr = buildSystemTimeString(System.currentTimeMillis());
    private static long cachedSystemMinute = -1;

    public static void renderSystemTimeHUD(DrawContext context) {
        if (!clock_system.shouldRender) return;

        MinecraftClient mc = MinecraftClient.getInstance();

        // update each minute
        long currentTime = System.currentTimeMillis();
        long minute = currentTime / 60000;
        if (minute != cachedSystemMinute) {
            cachedSystemMinute = minute;
            systemTimeStr = buildSystemTimeString(currentTime);
        }

        int width = 49;
        int height = 13;

        int x = Helper.defaultHUDLocationX(clock_system.originX, context, width) + clock_system.x;
        int y = Helper.defaultHUDLocationY(clock_system.originY, context, height) + clock_system.y;
        int color = clock_system.color | 0xFF000000;

        context.drawTexture(RenderLayer::getGuiTextured, CLOCK_SYSTEM, x, y, 0.0F, 0.0F, width, height, width, height, color);
        context.drawText(mc.textRenderer, systemTimeStr, x + 19, y + 3, color, false);
    }

    private static String buildSystemTimeString(long time) {
        return timeFormat.format(new Date(time));
    }
}
