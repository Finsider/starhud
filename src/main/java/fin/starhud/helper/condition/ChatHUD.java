package fin.starhud.helper.condition;

import net.minecraft.client.MinecraftClient;

public class ChatHUD {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.inGameHud.getChatHud().isChatFocused();
    }

    public static int getWidth() {
        return CLIENT.inGameHud.getChatHud().getWidth();
    }

    public static int getHeight() {
        return CLIENT.inGameHud.getChatHud().getHeight();
    }
}
