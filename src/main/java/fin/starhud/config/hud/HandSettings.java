package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class HandSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base;

    @Comment("Which way should the HUD goes when a the texture increases?")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public GrowthDirectionX textureGrowth;

    public boolean showCount = true;
    public boolean showDurability = true;

    public boolean drawBar = true;

    @Comment("Draw The Icon Using the Item instead of the HUD icon. (Warning: LARGE HUD)")
    public boolean drawItem = false;

    @ConfigEntry.ColorPicker
    public int color;

    public HandSettings(boolean shouldRender, int x, int y, ScreenAlignmentX originX, ScreenAlignmentY originY, GrowthDirectionX textureGrowth, int color) {
        base = new BaseHUDSettings(shouldRender, x, y, originX, originY);
        this.textureGrowth = textureGrowth;
        this.color = color;
    }
}
