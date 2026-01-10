package fin.starhud.config.hud.other;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class InventorySpaceSettings {
    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(false, 5, -19, ScreenAlignmentX.LEFT, ScreenAlignmentY.BOTTOM, GrowthDirectionX.RIGHT, GrowthDirectionY.UP);

    @ConfigEntry.ColorPicker
    public int color = 0xD8B58A;

    public boolean showRemaining = false;
    public boolean showMaxSlot = true;
}
