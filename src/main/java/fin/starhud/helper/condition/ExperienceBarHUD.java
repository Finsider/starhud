package fin.starhud.helper.condition;

import net.minecraft.client.MinecraftClient;

public class ExperienceBarHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.interactionManager != null && CLIENT.interactionManager.hasExperienceBar();
    }

    public static int getWidth() {
        return 182;
    }

    public static int getHeight() {
        return 5;
    }

}
