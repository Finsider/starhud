package fin.starhud.condition;

import net.minecraft.client.Minecraft;

public class FoodBarHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.gameMode != null && CLIENT.gameMode.canHurtPlayer();
    }

    public static int getWidth() {
        return 99; // same case as heatlh, assuming 10 food textures.
    }

    public static int getHeight() {
        return 9;
    }
}
