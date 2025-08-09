package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class CoordSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base;

    @ConfigEntry.ColorPicker
    public int color;

    public CoordSettings(BaseHUDSettings base, int color) {
        this.base = base;
        this.color = color;
    }
}
