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

    @Comment("Ping update interval, in seconds.")
    public double updateInterval = 5.0;

    @ConfigEntry.Gui.CollapsibleObject
    public PingColorSetting pingColor = new PingColorSetting();

    public static class PingColorSetting {
        @ConfigEntry.ColorPicker
        public int first = 0x85F290;
        @ConfigEntry.ColorPicker
        public int second = 0xECF285;
        @ConfigEntry.ColorPicker
        public int third = 0xFEBC49;
        @ConfigEntry.ColorPicker
        public int fourth = 0xFF5C71;
    }
}
