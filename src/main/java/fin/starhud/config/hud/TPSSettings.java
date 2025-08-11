package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TPSSettings {
    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, -116, -5, ScreenAlignmentX.RIGHT, ScreenAlignmentY.BOTTOM, GrowthDirectionX.LEFT, GrowthDirectionY.UP);

    @ConfigEntry.ColorPicker
    public int color = 0xb5ead7;

    public boolean useDynamicColor = true;

    public String additionalString = " TPS";
}
