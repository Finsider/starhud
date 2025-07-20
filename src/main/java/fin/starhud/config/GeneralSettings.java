package fin.starhud.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class GeneralSettings {

    @ConfigEntry.Gui.CollapsibleObject
    public EditHUDScreenSettings screenSettings = new EditHUDScreenSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public InGameHUDSettings inGameSettings = new InGameHUDSettings();

    public static class EditHUDScreenSettings {

        @Comment("Requires ImmediatelyFast mod, 3x Frametime improvement, at the cost of MANY rendering glitches.")
        public boolean shouldBatchHUDWithImmediatelyFast = false;

        @ConfigEntry.ColorPicker
        public int selectedBoxColor = 0x87ceeb;
    }

    public static class InGameHUDSettings {

        @Comment("Requires ImmediatelyFast mod, 3x Frametime improvement, with hardly any rendering glitches.")
        public boolean shouldBatchHUDWithImmediatelyFast = true;

        @Comment("Completely disable HUD Rendering.")
        public boolean disableHUDRendering = false;
    }
}
