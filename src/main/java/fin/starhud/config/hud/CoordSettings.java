package fin.starhud.config.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class CoordSettings {

    @ConfigEntry.Gui.TransitiveObject
    public BaseHUDSettings base = new BaseHUDSettings(true, 5, 5, ScreenAlignmentX.LEFT, ScreenAlignmentY.TOP);

    @ConfigEntry.Gui.CollapsibleObject
    public CoordPieceSetting X = new CoordPieceSetting(true, 0, 0,0xFc7871);

    @ConfigEntry.Gui.CollapsibleObject
    public CoordPieceSetting Y = new CoordPieceSetting(true, 0, 14,0xA6F1AF);

    @ConfigEntry.Gui.CollapsibleObject
    public CoordPieceSetting Z = new CoordPieceSetting(true, 0, 28,0x6CE1FC);

    public static class CoordPieceSetting {

        @Comment("Enable This Piece to Render")
        public boolean shouldRender;

        @Comment("X Offset to origin X location")
        public int xOffset;

        @Comment("Y Offset to origin Y location")
        public int yOffset;

        @ConfigEntry.ColorPicker
        public int color;

        public CoordPieceSetting(boolean shouldRender, int xOffset, int yOffset, int color) {
            this.shouldRender = shouldRender;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.color = color;
        }
    }
}
