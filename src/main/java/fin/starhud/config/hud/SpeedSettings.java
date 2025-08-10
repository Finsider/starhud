package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class SpeedSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 0, -150, ScreenAlignmentX.CENTER, ScreenAlignmentY.BOTTOM, GrowthDirectionX.CENTER, GrowthDirectionY.UP);

    @ConfigEntry.ColorPicker
    public int color = 0xffffff;
}
