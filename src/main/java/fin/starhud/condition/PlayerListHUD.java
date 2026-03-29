package fin.starhud.condition;

import fin.starhud.helper.Box;
import fin.starhud.mixin.accessor.AccessorPlayerListHud;
import net.minecraft.client.Minecraft;

public class PlayerListHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static final Box boundingBox = new Box(0,0, 0, 0);

    public static boolean isShown(String ignored) {
        return ((AccessorPlayerListHud) CLIENT.gui.getTabList()).isVisible();
    }

    public static int getWidth() {
        return boundingBox.getWidth();
    }

    public static int getHeight() {
        return boundingBox.getHeight();
    }
}
