package fin.starhud.screen;

import fin.starhud.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ActionBar {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    private Component text;
    private int remainingTime;

    public void render(GuiGraphics context, int centerX, int y) {
        float alpha = Math.min((float) this.remainingTime / 10, 1.0F);

        context.drawCenteredString(
                CLIENT.font,
                text,
                centerX, y,
                Helper.getWhite(alpha)
        );
    }

    public void setText(Component text) {
        this.text = text;
        this.remainingTime = 50;
    }

    public boolean isActive() {
        return this.remainingTime > 0;
    }

    public void tick() {
        if (isActive())
            --this.remainingTime;
    }
}
