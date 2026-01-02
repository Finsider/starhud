package fin.starhud.screen;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.helper.Box;
import fin.starhud.helper.PixelPlacement;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class SnapResult {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final GeneralSettings.EditHUDScreenSettings SETTINGS = Main.settings.generalSettings.screenSettings;

    boolean snappedX, snappedY;
    Integer snapLineX, snapLineY; // Screen positions of the snap lines
    Integer snapDeltaX, snapDeltaY;

    SnapResult(boolean snappedX, boolean snappedY, Integer snapLineX, Integer snapLineY, Integer snapDeltaX, Integer snapDeltaY) {
        this.snappedX = snappedX;
        this.snappedY = snappedY;
        this.snapLineX = snapLineX;
        this.snapLineY = snapLineY;
        this.snapDeltaX = snapDeltaX;
        this.snapDeltaY = snapDeltaY;
    }

    public void render(DrawContext context) {

        PixelPlacement.start(context);

        int screenWidth = MinecraftClient.getInstance().getWindow().getWidth();
        int screenHeight = MinecraftClient.getInstance().getWindow().getHeight();
        int color = SETTINGS.snapColor | 0xFF000000;

        // Draw vertical snap line
        if (snappedX && snapLineX != null) {
            context.fill(snapLineX, 0, snapLineX + 1, screenHeight, color);
        }
        // Draw horizontal snap line
        if (snappedY && snapLineY != null) {
            context.fill(0, snapLineY, screenWidth, snapLineY + 1, color);
        }

        PixelPlacement.end(context);
    }

    public static SnapResult getSnap(Box selectedHUDBox, List<AbstractHUD> selectedHUDs) {
        final int threshold = SETTINGS.getSnapThreshold();
        final int padding = SETTINGS.getSnapPadding();
        final int screenWidth = CLIENT.getWindow().getWidth();
        final int screenHeight = CLIENT.getWindow().getHeight();

        final int screenX = selectedHUDBox.getX();
        final int screenY = selectedHUDBox.getY();
        final int w = selectedHUDBox.getWidth();
        final int h = selectedHUDBox.getHeight();

        Integer snapScreenX = null, snapScreenY = null;
        Integer snapLineX = null, snapLineY = null; // Track where to draw the line
        int minDistX = threshold, minDistY = threshold;

        final int left = screenX;
        final int right = screenX + w;
        final int centerX = screenX + w / 2;
        final int top = screenY;
        final int bottom = screenY + h;
        final int centerY = screenY + h / 2;

        final int canvasLeft = padding;
        final int canvasRight = screenWidth - padding;
        final int canvasCenterX = screenWidth / 2;
        final int canvasTop = padding;
        final int canvasBottom = screenHeight - padding;
        final int canvasCenterY = screenHeight / 2;

        // Check canvas bounds - X axis
        int[][] xCanvasCandidates = {
                {left, canvasLeft},
                {right, canvasRight},
                {left,canvasCenterX}, {centerX, canvasCenterX}, {right, canvasCenterX}
        };

        for (int[] candidate : xCanvasCandidates) {
            int point = candidate[0];
            int target = candidate[1];
            int dist = Math.abs(point - target);
            if (dist < minDistX) {
                minDistX = dist;
                snapScreenX = target - (point - screenX);
                snapLineX = target;
            }
        }

        // Check canvas bounds - Y axis
        int[][] yCanvasCandidates = {
                {top, canvasTop},
                {bottom, canvasBottom},
                {top, canvasCenterY}, {centerY, canvasCenterY}, {bottom, canvasCenterY}
        };

        for (int[] candidate : yCanvasCandidates) {
            int point = candidate[0];
            int target = candidate[1];
            int dist = Math.abs(point - target);
            if (dist < minDistY) {
                minDistY = dist;
                snapScreenY = target - (point - screenY);
                snapLineY = target;
            }
        }

        // Check other HUDs
        for (AbstractHUD other : HUDComponent.getInstance().getRenderedHUDs()) {
            if (selectedHUDs.contains(other)) continue;

            final int oScreenX = other.getX();
            final int oScreenY = other.getY();
            final int oW = other.getTrueWidth();
            final int oH = other.getTrueHeight();

            final int oLeft = oScreenX;
            final int oRight = oScreenX + oW;
            final int oCenterX = oScreenX + oW / 2;
            final int oTop = oScreenY;
            final int oBottom = oScreenY + oH;
            final int oCenterY = oScreenY + oH / 2;

            // X-axis snapping
            int[][] xCandidates = {
                    {left, oLeft}, {left, oRight},
                    {right, oLeft}, {right, oRight},
                    {centerX, oCenterX}
            };

            for (int[] candidate : xCandidates) {
                int point = candidate[0];
                int target = candidate[1];
                int dist = Math.abs(point - target);
                if (dist < minDistX) {
                    minDistX = dist;
                    snapScreenX = target - (point - screenX);
                    snapLineX = target;
                }
            }

            // Y-axis snapping
            int[][] yCandidates = {
                    {top, oTop}, {top, oBottom},
                    {bottom, oTop}, {bottom, oBottom},
                    {centerY, oCenterY}
            };

            for (int[] candidate : yCandidates) {
                int point = candidate[0];
                int target = candidate[1];
                int dist = Math.abs(point - target);
                if (dist < minDistY) {
                    minDistY = dist;
                    snapScreenY = target - (point - screenY);
                    snapLineY = target;
                }
            }
        }

        Integer screenDeltaX = null;
        Integer screenDeltaY = null;
        if (snapScreenX != null) {
            screenDeltaX = snapScreenX - screenX;
        }

        if (snapScreenY != null) {
            screenDeltaY = snapScreenY - screenY;
        }

        return new SnapResult(
                snapScreenX != null,
                snapScreenY != null,
                snapLineX,
                snapLineY,
                screenDeltaX,
                screenDeltaY
        );
    }
}
