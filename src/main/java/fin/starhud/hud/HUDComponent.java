package fin.starhud.hud;

import fin.starhud.hud.implementation.*;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class HUDComponent {

    static HUDComponent instance;

    public final ArrayList<AbstractHUD> huds = new ArrayList<>();

    // separate status effect hud as they are rendered in a different place.
    public final AbstractHUD effectHUD;

    // should the HUD be rendered at ingame screen or the edit hud screen, very terrible.
    public boolean shouldRenderInGameScreen;

    // singleton
    private HUDComponent() {
        huds.add(new ArmorHUD());
        huds.add(new BiomeHUD());
        huds.add(new ClockInGameHUD());
        huds.add(new ClockSystemHUD());
        huds.add(new DayHUD());
        huds.add(new CoordinateHUD());
        huds.add(new DirectionHUD());
        huds.add(new FPSHUD());
        huds.add(new InventoryHUD());
        huds.add(new PingHUD());
        huds.add(new LeftHandHUD());
        huds.add(new RightHandHUD());
        huds.add(new TargetedCrosshairHUD());

        effectHUD = new EffectHUD();

        shouldRenderInGameScreen = true;
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

    public void setShouldRenderInGameScreen(boolean shouldRenderInGameScreen) {
        this.shouldRenderInGameScreen = shouldRenderInGameScreen;
    }

    public boolean shouldRenderInGameScreen() {
        return this.shouldRenderInGameScreen;
    }
}
