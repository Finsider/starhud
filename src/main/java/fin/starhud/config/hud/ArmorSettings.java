package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ArmorSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 5, -20, ScreenAlignmentX.LEFT, ScreenAlignmentY.MIDDLE, GrowthDirectionX.RIGHT, GrowthDirectionY.MIDDLE);

    @ConfigEntry.Gui.CollapsibleObject
    public ArmorPieceSetting helmet = new ArmorPieceSetting(true, 0, 0, true);

    @ConfigEntry.Gui.CollapsibleObject
    public ArmorPieceSetting chestplate = new ArmorPieceSetting(true, 0, 14, true);

    @ConfigEntry.Gui.CollapsibleObject
    public ArmorPieceSetting leggings = new ArmorPieceSetting(true, 0, 28, true);

    @ConfigEntry.Gui.CollapsibleObject
    public ArmorPieceSetting boots = new ArmorPieceSetting(true, 0, 42, true);

    public static class ArmorPieceSetting {

        @Comment("Enable This Piece to Render")
        public boolean shouldRender;

        public boolean drawBar;

        @Comment("Draw The Icon Using the Item instead of the HUD icon. (Warning: LARGE HUD)")
        public boolean drawItem = false;

        @Comment("X Offset to origin X location")
        public int xOffset;

        @Comment("Y Offset to origin Y location")
        public int yOffset;

        @ConfigEntry.ColorPicker
        public int color = 0xD0DAED;

        public ArmorPieceSetting(boolean shouldRender, int xOffset, int yOffset, boolean drawBar) {
            this.shouldRender = shouldRender;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.drawBar = drawBar;
        }
    }
}
