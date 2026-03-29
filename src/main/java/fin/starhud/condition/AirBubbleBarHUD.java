package fin.starhud.condition;

import net.minecraft.client.Minecraft;
import net.minecraft.tags.FluidTags;

public class AirBubbleBarHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static boolean isShown(String ignored) {
        if (CLIENT.gameMode == null || CLIENT.player == null) return false;
        return CLIENT.gameMode.canHurtPlayer() && CLIENT.player.isEyeInFluid(FluidTags.WATER) || CLIENT.player.getAirSupply() < CLIENT.player.getMaxAirSupply();
    }

    public static int getWidth() {
        return 99;
    }

    public static int getHeight() {
        return 9;
    }
}
