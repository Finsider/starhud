package fin.starhud.condition;

import fin.starhud.mixin.accessor.AccessorChatHud;
import net.minecraft.client.Minecraft;

public class ChatHUD {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.gui.getChat().isChatFocused();
    }

    public static int getWidth() {
        return ((AccessorChatHud) CLIENT.gui.getChat()).starhud$getWidth();
    }

    public static int getHeight() {
        return ((AccessorChatHud) CLIENT.gui.getChat()).starhud$getHeight();
    }
}
