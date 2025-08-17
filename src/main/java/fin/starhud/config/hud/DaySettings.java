package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class DaySettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 5, 47, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN);

    @ConfigEntry.ColorPicker
    public int color = 0xFFFFFF;

    public String additionalString = "Day ";
}
