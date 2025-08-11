package fin.starhud.helper.condition;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class HealthBarHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static int height;
    private static int cachedHeartAmount = -1;

    public static boolean isShown(String ignored) {
        return CLIENT.interactionManager != null && CLIENT.interactionManager.hasStatusBars();
    }

    // assuming 10 health textures + 9 gaps
    // each texture consist of 9x9 px, with 1 pixel for gap
    // so 10 (health) * ( 9 (width) + 1 (gap) ) - 1 (one unneeded gap)
    public static int getWidth() {
        return (10 * (9 + 1)) - 1;
    }

    public static int getHeight() {
        PlayerEntity player = CLIENT.player;

        if (player == null) return -1;

        float maxHealth = player.getMaxHealth();
        int absorption = MathHelper.ceil(player.getAbsorptionAmount());

        int currentHeartAmount = (int)(maxHealth + absorption);
        if (currentHeartAmount == cachedHeartAmount) {
            return height;
        }
        cachedHeartAmount = currentHeartAmount;

        // Calculate hearts using the same logic as renderHealthBar
        int regularHearts = MathHelper.ceil((double)maxHealth / 2.0F);
        int absorptionHearts = MathHelper.ceil((double)absorption / 2.0F);
        int totalHearts = regularHearts + absorptionHearts;

        int p = MathHelper.ceil((maxHealth + (float)absorption) / 2.0F / 10.0F);
        int lineHeight = Math.max(10 - (p - 2), 3);

        int rows = (totalHearts - 1) / 10 + 1;
        height = (rows - 1) * lineHeight + 9;

        return height;
    }


}
