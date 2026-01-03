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

        @Comment("Requires ImmediatelyFast mod, 3x Frametime improvement, at the cost of MANY rendering glitches.")
        public boolean shouldBatchHUDWithImmediatelyFast = false;

        public boolean drawBorder = true;
        public boolean drawGrid = true;
        public boolean drawDarkBackground = true;

        public int darkOpacity = 35;

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int gridColor = 0x20A8E6E6;

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int selectedBoxColor = 0x8087ceeb;

        @ConfigEntry.ColorPicker(allowAlpha = true)
        public int selectedGroupBoxColor = 0x80Fc7871;

        @ConfigEntry.ColorPicker
        public int dragBoxColor = 0xa8d8ea;

        @ConfigEntry.ColorPicker
        public int snapColor = 0x00E5FF;

        public int snapPadding = 24;

        public int snapThreshold = 8;

        public int getSnapPadding() {
            if (snapPadding < 0)
                snapPadding = 0;
            return this.snapPadding;
        }

        public int getSnapThreshold() {
            if (snapThreshold < 1)
                snapThreshold = 5;
            return snapThreshold;
        }

        public int getDarkOpacity() {
            if (darkOpacity < 0 || darkOpacity > 100)
                darkOpacity = 50;

            return darkOpacity;
        }
    }

    public static class InGameHUDSettings {

        @Comment("Requires ImmediatelyFast mod, 3x Frametime improvement, with hardly any rendering glitches.")
        public boolean shouldBatchHUDWithImmediatelyFast = true;

        @Comment("Completely disable HUD Rendering.")
        public boolean disableHUDRendering = false;
    }

    public static class HUDSettings {

        @Comment("Set the scale to 0 for default GUI Scale.")
        public float globalScale = 0.0f;

        @Comment("The Interval between each data collection, the higher the longer it takes for the hud to update.")
        public float dataCollectionInterval = 0.1F;

        @Comment("Since some fonts may have a different font heights, try adjusting this to your liking. Default is 0.")
        public int textYOffset = 0;

        public int textPadding = 5;

        public int iconInfoGap = 1;

        @Comment("Either draw the background rounded or rectangle")
        public boolean drawBackgroundRounded = true;

        public float getGlobalScale() {
            if (this.globalScale < 0) {
                this.globalScale = 0;
            }

            return this.globalScale;
        }
    }
}