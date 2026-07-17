package fin.starhud.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface HUDInterface {

    boolean render(GuiGraphicsExtractor context);

    boolean collect();

    boolean shouldRender();

    void update();

    String getId();
}
