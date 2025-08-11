package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class PingSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, -57, -5, ScreenAlignmentX.RIGHT, ScreenAlignmentY.BOTTOM, GrowthDirectionX.LEFT, GrowthDirectionY.UP);

    @ConfigEntry.ColorPicker
    public int color = 0xa4d8e1;

    public boolean useDynamicColor = true;

    @Comment("Ping update interval, in seconds.")
    public double updateInterval = 5.0;

    public String additionalString = " ms";
}
