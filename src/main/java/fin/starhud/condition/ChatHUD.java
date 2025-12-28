package fin.starhud.condition;

import fin.starhud.mixin.accessor.AccessorChatHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;

public class ChatHUD {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.currentScreen instanceof ChatScreen;
    }

    public static int getWidth() {
        return ((AccessorChatHud) CLIENT.inGameHud.getChatHud()).starhud$getWidth();
    }

    public static int getHeight() {
        return ((AccessorChatHud) CLIENT.inGameHud.getChatHud()).starhud$getHeight();
    }
}
