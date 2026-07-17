package fin.starhud.condition;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;

public class OffHandHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static boolean isShown(String ignored) {
        return !CLIENT.player.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty();
    }

    public static int getWidth() {
        return 29;
    }

    public static int getHeight() {
        return 24;
    }

}
