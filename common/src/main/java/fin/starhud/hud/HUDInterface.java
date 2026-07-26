package fin.starhud.hud;

import net.minecraft.client.gui.GuiGraphics;

public interface HUDInterface {

    boolean render(GuiGraphics context);

    boolean collect();

    boolean shouldRender();

    void update();

    String getId();
}
