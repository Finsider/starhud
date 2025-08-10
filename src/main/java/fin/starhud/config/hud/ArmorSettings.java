package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ArmorSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base;

    @ConfigEntry.Gui.TransitiveObject
    public DurabilitySettings durabilitySettings = new DurabilitySettings();

    @ConfigEntry.ColorPicker
    public int color = 0xD0DAED;

    public ArmorSettings(BaseHUDSettings base) {
        this.base = base;
    }
}
