package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class BiomeSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 0, 5, ScreenAlignmentX.CENTER, ScreenAlignmentY.TOP);

    @Comment("Which way should the HUD goes when a the texture increases?")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public GrowthDirectionX textGrowth = GrowthDirectionX.CENTER;

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
