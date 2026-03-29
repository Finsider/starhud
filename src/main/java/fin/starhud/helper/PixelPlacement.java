package fin.starhud.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class PixelPlacement {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static void start(GuiGraphicsExtractor context) {
        context.pose().pushMatrix();
        context.pose().scale(1.0f / CLIENT.getWindow().getGuiScale());
    }

    public static void end(GuiGraphicsExtractor context) {
        context.pose().popMatrix();
    }
}
