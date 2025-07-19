package fin.starhud.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class GeneralSettings {

    @ConfigEntry.Gui.CollapsibleObject
    public EditHUDScreenSettings screenSettings = new EditHUDScreenSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public InGameHUDSettings inGameSettings = new InGameHUDSettings();

    public static class EditHUDScreenSettings {
        public boolean shouldBatchHUDWithImmediatelyFast = false;

        @ConfigEntry.ColorPicker
        public int selectedBoxColor = 0x87ceeb;
    }

    public static class InGameHUDSettings {
        public boolean shouldBatchHUDWithImmediatelyFast = true;

        public boolean disableHUDRendering = false;
    }
}
