package fin.starhud.config;

import fin.starhud.helper.Condition;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ConditionalSettings {

    @Comment("Condition.")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Condition condition;

    @Comment("Enable this HUD To be Rendered")
    public boolean shouldRender = true;

    @Comment("Shifts this HUD in the X Axis")
    public int xOffset = 0;

    @Comment("Shifts this HUD in the Y Axis")
    public int yOffset = 0;

    public ConditionalSettings() {
        this.condition = Condition.DEBUG_HUD_OPENED;
    }

    public boolean isConditionMet() {
        return this.condition.isConditionMet();
    }
}