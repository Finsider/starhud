package fin.starhud.condition;

import fin.starhud.helper.Box;
import net.minecraft.client.Minecraft;

public class ScoreboardHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    private static final Box boundingBox = new Box(0,0);

    public static boolean isShown(String ignored) {
        if (CLIENT.level == null) return false;
        return CLIENT.level.getScoreboard().getObjectives().isEmpty();
    }

    // unfortunately scoreboard rendering logic in 1.21.1 is blocked behind context.draw()
    // thus we can't capture the rendering box that easily, we can resort to the most excessive function ever, but I won't do that.
    // hence I'll just give it 0.
    public static int getWidth() {
        return boundingBox.getWidth();
    }

    public static int getHeight() {
        return boundingBox.getHeight();
    }

    public static void captureBoundingBox(int x1, int y1, int x2, int y2) {
        boundingBox.setBoundingBox(x1, y1, x2 - x1, y2 - y1);
    }
}
