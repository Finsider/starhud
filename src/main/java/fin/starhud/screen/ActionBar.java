package fin.starhud.screen;

import fin.starhud.Helper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ActionBar {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private Text text;
    private int remainingTime;

    public void render(DrawContext context, int centerX, int y) {
        float alpha = Math.min((float) this.remainingTime / 10, 1.0F);

        context.drawCenteredTextWithShadow(
                CLIENT.textRenderer,
                text,
                centerX, y,
                Helper.getWhite(alpha)
        );
    }

    public void setText(Text text) {
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
