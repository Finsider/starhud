package fin.starhud.condition;

import fin.starhud.helper.Box;
import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.DisplaySlot;

public class ScoreboardHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static boolean isShown(String ignored) {
        if (CLIENT.level == null) return false;
        return CLIENT.level.getScoreboard().getDisplayObjective(DisplaySlot.SIDEBAR) != null;
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
