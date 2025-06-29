package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class DaySettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 5, 47, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP);

    @Comment("Which way should the HUD goes when a the texture increases?")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public GrowthDirectionX textGrowth = GrowthDirectionX.RIGHT;

    @ConfigEntry.ColorPicker
    public int color = 0xFFFFFF;
}
