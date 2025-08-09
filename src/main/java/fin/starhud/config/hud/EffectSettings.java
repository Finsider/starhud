package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class EffectSettings implements ConfigData {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base;

    @Comment("Render the HUD Vertically, (Recommended to switch Different Type Gap with Same Type Gap if this is toggled.)")
    public boolean drawVertical = false;

    @Comment("Draw the Timer with The Effect Color")
    public boolean useEffectColor = false;

    @Comment("Gap between the same type Effect HUD.")
    public int sameTypeGap = 1;

    @ConfigEntry.ColorPicker
    public int ambientColor = 0xd5feef;

    @ConfigEntry.ColorPicker
    public int infiniteColor = 0xB5D0E8;

    public EffectSettings(BaseHUDSettings base) {
        this.base = base;
    }
}
