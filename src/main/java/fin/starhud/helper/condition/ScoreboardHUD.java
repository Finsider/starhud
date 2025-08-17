package fin.starhud.helper.condition;

import fin.starhud.helper.Box;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;

public class ScoreboardHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isShown(String ignored) {
        return CLIENT.world.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR) != null;
    }

    // unfortunately scoreboard rendering logic in 1.21.1 is blocked behind context.draw()
    // thus we can't capture the rendering box that easily, we can resort to the most excessive function ever, but I won't do that.
    // hence I'll just give it 0.
    public static int getWidth() {
        return 0;
    }

    public static int getHeight() {
        return 0;
    }
}