package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ClockInGameSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, -29, 19, ScreenAlignmentX.CENTER, ScreenAlignmentY.TOP, GrowthDirectionX.CENTER, GrowthDirectionY.DOWN);

    public boolean use12Hour = false;

    @ConfigEntry.Gui.CollapsibleObject
    public ClockInGameColorSetting color = new ClockInGameColorSetting();

    public static class ClockInGameColorSetting {
        @ConfigEntry.ColorPicker
        public int day = 0xfff9b5;
        @ConfigEntry.ColorPicker
        public int night = 0xd6cbef;
        @ConfigEntry.ColorPicker
        public int rain = 0xb5d0e8;
        @ConfigEntry.ColorPicker
        public int thunder = 0x8faecb;
    }
}
