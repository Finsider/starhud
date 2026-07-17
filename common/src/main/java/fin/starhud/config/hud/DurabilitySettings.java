package fin.starhud.config.hud;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class DurabilitySettings {

    @Comment("Draw The Icon Using the Item instead of the HUD icon. (Warning: LARGE HUD)")
    public boolean drawItem = false;

    @Comment("Draw The HUD With Dynamic Color according to how much durability is available.")
    public boolean useDynamicColor = true;

    @Comment("Durability Display Mode")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public DisplayMode displayMode = DisplayMode.BAR;

    public DisplayMode getDisplayMode() {
        if (displayMode == null) displayMode = DisplayMode.BAR;

        return displayMode;
    }

    public enum DisplayMode {
        BAR,
        FRACTIONAL,
        VALUE_ONLY,
        PERCENTAGE,
        COMPACT;
    }
}
