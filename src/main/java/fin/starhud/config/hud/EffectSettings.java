package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class EffectSettings implements ConfigData {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, -5, 5, ScreenAlignmentX.RIGHT, ScreenAlignmentY.TOP, GrowthDirectionX.LEFT, GrowthDirectionY.DOWN);

    @Comment("Render the HUD Vertically, (Recommended to switch Different Type Gap with Same Type Gap if this is toggled.)")
    public boolean drawVertical = false;

    @Comment("Gap between Negative and Positive Effect HUD. (Recommendation) (if Vertical 26, Horizontal 34)")
    public int differentTypeGap = 34;

    @Comment("Gap between the same type Effect HUD. (Recommendation) (if Vertical 34, Horizontal 25)")
    public int sameTypeGap = 25;

    @Comment("Draw the Timer with The Effect Color")
    public boolean useEffectColor = false;

    @ConfigEntry.ColorPicker
    public int ambientColor = 0xd5feef;

    @ConfigEntry.ColorPicker
    public int infiniteColor = 0xB5D0E8;

}
