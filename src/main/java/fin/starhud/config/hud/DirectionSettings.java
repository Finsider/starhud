package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class DirectionSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 1, 19, ScreenAlignmentX.CENTER, ScreenAlignmentY.TOP, GrowthDirectionX.RIGHT, GrowthDirectionY.DOWN);

    public boolean includeOrdinal = false;

    @ConfigEntry.Gui.CollapsibleObject
    public DirectionColorSetting directionColor = new DirectionColorSetting();

    public static class DirectionColorSetting {
        @ConfigEntry.ColorPicker
        public int s = 0xffb5b5;
        @ConfigEntry.ColorPicker
        public int sw = 0xffcbb3;
        @ConfigEntry.ColorPicker
        public int w = 0xffd1b7;
        @ConfigEntry.ColorPicker
        public int nw = 0xd8cae8;
        @ConfigEntry.ColorPicker
        public int n = 0xb7c9e9;
        @ConfigEntry.ColorPicker
        public int ne = 0xd4dbf0;
        @ConfigEntry.ColorPicker
        public int e = 0xffe5b4;
        @ConfigEntry.ColorPicker
        public int se = 0xffd0c4;
    }
}
