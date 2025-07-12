package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class BiomeSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 0, 5, ScreenAlignmentX.CENTER, ScreenAlignmentY.TOP, GrowthDirectionX.CENTER, GrowthDirectionY.DOWN);

    @ConfigEntry.Gui.CollapsibleObject
    public DimensionColorSetting color = new DimensionColorSetting();

    public static class DimensionColorSetting {
        @ConfigEntry.ColorPicker
        public int overworld = 0xFFFFFF;
        @ConfigEntry.ColorPicker
        public int nether = 0xfc7871;
        @ConfigEntry.ColorPicker
        public int end = 0xc9c7e3;
        @ConfigEntry.ColorPicker
        public int custom = 0xFFFFFF;
    }
}
