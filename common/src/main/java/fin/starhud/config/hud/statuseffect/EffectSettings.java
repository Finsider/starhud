package fin.starhud.config.hud.statuseffect;

import fin.starhud.config.BaseHUDSettings;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class EffectSettings implements ConfigData {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base;

    @Comment("Render the HUD Vertically")
    public boolean drawVertical = false;

    @Comment("Combine all the hud background.")
    public boolean combineBackground = false;

    @Comment("Gap between the same type Effect HUD.")
    public int sameTypeGap = 1;

    @ConfigEntry.ColorPicker
    public int customColor = 0xFFFFFF;

    public EffectSettings(BaseHUDSettings base, int customColor) {
        this.base = base;
        this.customColor = customColor;
    }
}
