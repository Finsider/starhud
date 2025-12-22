package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class EffectSettings implements ConfigData {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base;

    @Comment("Draw the HUD in a clock like fashion instead of a bars.")
    public boolean drawTimer = false;

    @Comment("Render the HUD Vertically, (Recommended to switch Different Type Gap with Same Type Gap if this is toggled.)")
    public boolean drawVertical = false;

    @Comment("Also draw Status Effect that are supposedly Hidden.")
    public boolean drawHidden = false;

    @Comment("Combine all the hud background.")
    public boolean combineBackground = false;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ColorMode colorMode = ColorMode.DYNAMIC;

    @Comment("Gap between the same type Effect HUD.")
    public int sameTypeGap = 1;

    @ConfigEntry.ColorPicker
    public int ambientColor = 0xd5feef;

    @ConfigEntry.ColorPicker
    public int infiniteColor = 0xB5D0E8;

    @ConfigEntry.ColorPicker
    public int customColor = 0xFFFFFF;

    public EffectSettings(BaseHUDSettings base, int customColor) {
        this.base = base;
        this.customColor = customColor;
    }

    public ColorMode getColorMode() {
        if (colorMode == null) colorMode = ColorMode.DYNAMIC;
        return colorMode;
    }

    public enum ColorMode {
        CUSTOM,
        EFFECT,
        DYNAMIC
    }
}
