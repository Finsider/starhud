package fin.starhud.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class PixelPlacement {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static void start(GuiGraphics context) {
        context.pose().pushMatrix();
        context.pose().scale(1.0f / CLIENT.getWindow().getGuiScale());
    }

    public static void end(GuiGraphics context) {
        context.pose().popMatrix();
    }
}
