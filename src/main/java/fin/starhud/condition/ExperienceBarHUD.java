package fin.starhud.condition;

import net.minecraft.client.Minecraft;

public class ExperienceBarHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.gameMode != null && CLIENT.gameMode.hasExperience();
    }

    public static int getWidth() {
        return 182;
    }

    public static int getHeight() {
        return 5;
    }

}
