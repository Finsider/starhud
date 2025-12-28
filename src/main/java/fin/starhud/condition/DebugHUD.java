package fin.starhud.condition;

import net.minecraft.client.MinecraftClient;

public class DebugHUD {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.options.debugEnabled;
    }
}
