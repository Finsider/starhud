package fin.starhud.hud;

import net.minecraft.client.gui.DrawContext;

public interface HUDInterface {

    boolean render(DrawContext context);

    boolean shouldRender();

    void update();
}
