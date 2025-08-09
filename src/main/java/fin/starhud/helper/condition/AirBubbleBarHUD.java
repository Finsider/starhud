package fin.starhud.helper.condition;

import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.FluidTags;

public class AirBubbleBarHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.interactionManager.hasStatusBars() && CLIENT.player.isSubmergedIn(FluidTags.WATER) || CLIENT.player.getAir() < CLIENT.player.getMaxAir();
    }

    public static int getWidth() {
        return 99;
    }

    public static int getHeight() {
        return 9;
    }
}
