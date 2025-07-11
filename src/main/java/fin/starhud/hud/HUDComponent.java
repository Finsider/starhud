package fin.starhud.hud;

import fin.starhud.hud.implementation.*;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class HUDComponent {

    static HUDComponent instance;

    public final ArrayList<AbstractHUD> huds = new ArrayList<>();

    // separate status effect hud as they are rendered in a different place.
    public final AbstractHUD effectHUD;

    // singleton
    private HUDComponent() {
        huds.add(new Armor());
        huds.add(new Biome());
        huds.add(new ClockInGame());
        huds.add(new ClockSystem());
        huds.add(new Day());
        huds.add(new Coordinate());
        huds.add(new Direction());
        huds.add(new FPS());
        huds.add(new Inventory());
        huds.add(new Ping());
        huds.add(new LeftHand());
        huds.add(new RightHand());
        huds.add(new TargetedCrosshair());

        effectHUD = new Effect();
    }

    public static HUDComponent getInstance() {
        if (instance == null) {
            instance = new HUDComponent();
        }
        return instance;
    }

    public void renderAll(DrawContext context) {
        for (HUDInterface hud : huds) {
            if (hud.shouldRender())
                hud.render(context);
        }
    }

    public void updateAll() {
        for (HUDInterface hud : huds) {
            hud.update();
        }

        effectHUD.update();
    }
}
