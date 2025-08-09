package fin.starhud.helper.condition;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;

public class ChatHUD {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.currentScreen instanceof ChatScreen;
    }

    public static int getWidth() {
        return CLIENT.inGameHud.getChatHud().getWidth();
    }

    public static int getHeight() {
        return CLIENT.inGameHud.getChatHud().getHeight();
    }
}
