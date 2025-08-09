package fin.starhud.helper.condition;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;

public class OffHandHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isShown(String ignored) {
        return !CLIENT.player.getEquippedStack(EquipmentSlot.OFFHAND).isEmpty();
    }

    public static int getWidth() {
        return 29;
    }

    public static int getHeight() {
        return 24;
    }

}
