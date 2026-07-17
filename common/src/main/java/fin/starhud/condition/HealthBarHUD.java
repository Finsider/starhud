package fin.starhud.condition;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class HealthBarHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    private static int height;
    private static int cachedHeartAmount = -1;

    public static boolean isShown(String ignored) {
        return CLIENT.gameMode != null && CLIENT.gameMode.canHurtPlayer();
    }

    // assuming 10 health textures + 9 gaps
    // each texture consist of 9x9 px, with 1 pixel for gap
    // so 10 (health) * ( 9 (width) + 1 (gap) ) - 1 (one unneeded gap)
    public static int getWidth() {
        return (10 * (9 + 1)) - 1;
    }

    public static int getHeight() {
        Player player = CLIENT.player;

        if (player == null) return -1;

        float maxHealth = player.getMaxHealth();
        int absorption = (int) Math.ceil(player.getAbsorptionAmount());

        int currentHeartAmount = (int)(maxHealth + absorption);
        if (currentHeartAmount == cachedHeartAmount) {
            return height;
        }
        cachedHeartAmount = currentHeartAmount;

        // Calculate hearts using the same logic as renderHealthBar
        double regularHearts = Math.ceil((double)maxHealth / 2.0F);
        double absorptionHearts = Math.ceil((double)absorption / 2.0F);
        double totalHearts = regularHearts + absorptionHearts;

        int p = (int) Math.ceil((maxHealth + (float)absorption) / 2.0F / 10.0F);
        int lineHeight = Math.max(10 - (p - 2), 3);

        int rows = (int) (totalHearts - 1) / 10 + 1;
        height = (rows - 1) * lineHeight + 9;

        return height;
    }


}
