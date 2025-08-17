package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class SpeedSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 70, -5, ScreenAlignmentX.LEFT, ScreenAlignmentY.BOTTOM, GrowthDirectionX.RIGHT, GrowthDirectionY.UP);

    @ConfigEntry.ColorPicker
    public int color = 0xb5d0e8;

    public String additionalString = " BPS";
}
