package fin.starhud.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class ActionBar {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    private Component text;
    private int remainingTime;

    public void render(GuiGraphicsExtractor context, int centerX, int y) {
        float alpha = Math.min((float) this.remainingTime / 10, 1.0F);

        context.centeredText(
                CLIENT.font,
                text,
                centerX, y,
                ARGB.white(alpha)
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
