package fin.starhud.helper.condition;

import fin.starhud.helper.Box;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;

public class ScoreboardHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static final Box boundingBox = new Box(0,0);

    public static boolean isShown(String ignored) {
        return CLIENT.world.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR) != null;
    }

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
