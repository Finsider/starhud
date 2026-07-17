package fin.starhud.condition;

import net.minecraft.client.Minecraft;

public class ArmorBarHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static boolean isShown(String ignored) {
        if (CLIENT.gameMode == null || CLIENT.player == null) return false;
        return CLIENT.gameMode.canHurtPlayer() && CLIENT.player.getArmorValue() > 0;
    }

    public static int getWidth() {
        return 99; // same case as heatlh, assuming 10 food textures.
    }

    public static int getHeight() {
        return 9;
    }

}
