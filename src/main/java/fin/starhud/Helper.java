package fin.starhud;

import fin.starhud.config.GeneralSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;


public class Helper {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final GeneralSettings.HUDSettings HUD_SETTINGS = Main.settings.generalSettings.hudSettings;

    private static final char[] superscripts = "⁰¹²³⁴⁵⁶⁷⁸⁹".toCharArray();
    private static final char[] subscripts = "₀₁₂₃₄₅₆₇₈₉".toCharArray();

    // only convert numbers.
    public static String toSuperscript(String str) {
        char[] chars = str.toCharArray();

        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char c = chars[i];

            if (c >= '0' && c <= '9')
                chars[i] = superscripts[c - '0'];
        }

        return new String(chars);
    }

    public static String toSubscript(String str) {
        char[] chars = str.toCharArray();

        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char c = chars[i];

            if (c >= '0' && c <= '9')
                chars[i] = subscripts[c - '0'];
        }

        return new String(chars);
    }

    // convert (modname:snake_case) into (Snake Case)
    public static String idNameFormatter(String id) {

        // trim every character from ':' until first index
        id = id.substring(id.indexOf(':') + 1);

        char[] chars = id.toCharArray();

        if (chars.length == 0) return "-";

        chars[0] = Character.toUpperCase(chars[0]);
        for (int i = 1; i < chars.length; ++i) {
            if (chars[i] != '_') continue;

            chars[i] = ' ';

            // capitalize the first character after spaces
            if (i + 1 < chars.length) {
                chars[i + 1] = Character.toUpperCase(chars[i + 1]);
            }
        }

        return new String(chars);
    }

    public static boolean withinRange(int u, int v, int range) {
        int left = v - range;
        int right = v + range;

        return left <= u && u <= right;
    }

    // color transition from pastel (red to green).
    public static int getItemBarColor(int stackStep, int maxStep) {
        return MathHelper.hsvToRgb(0.35F * stackStep / (float) maxStep, 0.45F, 0.95F);
    }

    public static float getGlobalScale() {
        if (HUD_SETTINGS.getGlobalScale() == 0) {
            return (float) CLIENT.getWindow().getScaleFactor();
        }
        return HUD_SETTINGS.getGlobalScale();
    }

    public static String buildMinecraftTime24String(int hours, int minutes) {
        StringBuilder timeBuilder = new StringBuilder();

        if (hours < 10) timeBuilder.append('0');
        timeBuilder.append(hours).append(':');

        if (minutes < 10) timeBuilder.append('0');
        timeBuilder.append(minutes);

        return timeBuilder.toString();
    }

    public static String buildMinecraftTime12String(int hours, int minutes) {
        StringBuilder timeBuilder = new StringBuilder();

        String period = hours >= 12 ? " PM" : " AM";

        // 01.00 until 12.59 AM / PM
        hours %= 12;
        if (hours == 0) hours = 12;

        timeBuilder.append(buildMinecraftTime24String(hours, minutes)).append(period);

        return timeBuilder.toString();
    }

    public static String getModName(Identifier id) {
        String nameSpace = id.getNamespace();
        ModContainer container = FabricLoader.getInstance().getModContainer(nameSpace).orElse(null);
        return container == null ? nameSpace : container.getMetadata().getName();
    }

    public static boolean isModPresent(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    public static int getStep(int curr, int max, int maxStep) {
        return MathHelper.clamp(Math.round((float) curr * maxStep / (float) max), 0, maxStep);
    }
}