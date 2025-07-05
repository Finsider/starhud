package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TargetedCrosshairSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 0, 50, ScreenAlignmentX.CENTER, ScreenAlignmentY.TOP);

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public GrowthDirectionX textureGrowth = GrowthDirectionX.CENTER;

    @ConfigEntry.ColorPicker
    public int modNameColor = 0xddebf5;

    @ConfigEntry.ColorPicker
    public int targetedNameColor = 0xFFFFFF;

    @ConfigEntry.Gui.CollapsibleObject
    public Colors entityColors = new Colors();

    public static class Colors {
        @ConfigEntry.ColorPicker
        public int hostile = 0xF6A5A5;

        @ConfigEntry.ColorPicker
        public int angerable = 0xF7D6B7;

        @ConfigEntry.ColorPicker
        public int passive = 0xcbf0d8;

        @ConfigEntry.ColorPicker
        public int player = 0xA8D8EA;

        @ConfigEntry.ColorPicker
        public int unknown = 0xC5C3D4;
    }

}
