package fin.starhud.hud;

import net.minecraft.client.gui.DrawContext;

public interface HUDInterface {

    boolean render(DrawContext context);

    // if not enabled, or "Hide On" condition is enabled and are active.
    boolean shouldRender();

    void update();
}
