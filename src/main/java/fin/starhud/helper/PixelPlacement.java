package fin.starhud.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class PixelPlacement {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static void start(DrawContext context) {
        context.getMatrices().pushMatrix();
        context.getMatrices().scale(1.0f / CLIENT.getWindow().getScaleFactor());
    }

    public static void end(DrawContext context) {
        context.getMatrices().popMatrix();
    }
}
