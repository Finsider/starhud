package fin.starhud.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class PixelPlacement {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static void start(DrawContext context) {
        context.getMatrices().push();
        final float sc = (float) (1 / CLIENT.getWindow().getScaleFactor());
        context.getMatrices().scale(sc, sc, 1.0f);
    }

    public static void end(DrawContext context) {
        context.getMatrices().pop();
    }
}
