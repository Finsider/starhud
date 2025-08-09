package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class HandSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base;

    @ConfigEntry.Gui.TransitiveObject
    public DurabilitySettings durabilitySettings = new DurabilitySettings();

    public boolean showCount = true;
    public boolean showDurability = true;

    @ConfigEntry.ColorPicker
    public int color;

    public HandSettings(boolean shouldRender, int x, int y, ScreenAlignmentX originX, ScreenAlignmentY originY, GrowthDirectionX growthDirectionX, GrowthDirectionY growthDirectionY, int color) {
        base = new BaseHUDSettings(shouldRender, x, y, originX, originY, growthDirectionX, growthDirectionY);
        this.color = color;
    }
}
