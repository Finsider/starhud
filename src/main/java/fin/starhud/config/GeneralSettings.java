package fin.starhud.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class GeneralSettings {

    @ConfigEntry.Gui.CollapsibleObject
    public EditHUDScreenSettings screenSettings = new EditHUDScreenSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public InGameHUDSettings inGameSettings = new InGameHUDSettings();

    public static class EditHUDScreenSettings {

        @ConfigEntry.ColorPicker
        public int selectedBoxColor = 0x87ceeb;
    }

    public static class InGameHUDSettings {

        @Comment("Completely disable HUD Rendering.")
        public boolean disableHUDRendering = false;
    }
}