package fin.starhud.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class GeneralSettings {

    @ConfigEntry.Gui.CollapsibleObject
    public EditHUDScreenSettings screenSettings = new EditHUDScreenSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public InGameHUDSettings inGameSettings = new InGameHUDSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public HUDSettings hudSettings = new HUDSettings();

    public static class EditHUDScreenSettings {

        public boolean drawBorder = true;
        public boolean drawGrid = true;

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int selectedBoxColor = 0x8087ceeb;

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int selectedGroupBoxColor = 0x80Fc7871;

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int gridColor = 0x20A8E6E6;

        @ConfigEntry.ColorPicker
        public int dragBoxColor = 0xa8d8ea;

        public int gridEdgePadding = 5;
    }

    public static class InGameHUDSettings {

        @Comment("Completely disable HUD Rendering.")
        public boolean disableHUDRendering = false;
    }

    public static class HUDSettings {

        @Comment("The Interval between each data collection, the higher the longer it takes for the hud to update.")
        public float dataCollectionInterval = 0.1F;

        @Comment("Since some fonts may have a different font heights, try adjusting this to your liking. Default is 0.")
        public int textYOffset = 0;

        public int textPadding = 5;

        public int iconInfoGap = 1;

        @Comment("Either draw the background rounded or rectangle")
        public boolean drawBackgroundRounded = true;
    }
}