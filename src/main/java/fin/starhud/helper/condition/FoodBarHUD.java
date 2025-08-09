package fin.starhud.helper.condition;

import net.minecraft.client.MinecraftClient;

public class FoodBarHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.interactionManager.hasStatusBars();
    }

    public static int getWidth() {
        return 99; // same case as heatlh, assuming 10 food textures.
    }

    public static int getHeight() {
        return 9;
    }
}
