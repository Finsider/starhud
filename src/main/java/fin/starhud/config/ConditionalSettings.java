package fin.starhud.config;

import fin.starhud.helper.Condition;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ConditionalSettings {

    @Comment("Condition.")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Condition condition = Condition.DEBUG_HUD_OPENED;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public PositionMode positionMode = PositionMode.NONE;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public RenderMode renderMode = RenderMode.SHOW;

    @Comment("Server IP Address and Mod ID requires this field.")
    public String additionalString = "";

    @Comment("Shifts this HUD in the X Axis")
    public int xOffset = 0;

    @Comment("Shifts this HUD in the Y Axis")
    public int yOffset = 0;

    public boolean renderIfActive() {
        return (renderMode == RenderMode.RENDER_IF_ACTIVE && isConditionMet());
    }

    public boolean shouldRender() {
        return !shouldHide();
    }

    public boolean shouldHide() {
        return (renderMode == RenderMode.HIDE && isConditionMet());
    }

    public int getXOffset(float scaleFactor) {
        if (positionMode == null) positionMode = PositionMode.NONE;

        return switch (positionMode) {
            case ADD_WIDTH -> xOffset + (int) (condition.getWidth() * scaleFactor);
            case SUBTRACT_WIDTH -> xOffset - (int) (condition.getWidth() * scaleFactor);
            default -> xOffset;
        };
    }

    public int getYOffset(float scaleFactor) {
        if (positionMode == null) positionMode = PositionMode.NONE;

        return switch (positionMode) {
            case ADD_HEIGHT -> yOffset + (int) (condition.getHeight() * scaleFactor);
            case SUBTRACT_HEIGHT -> yOffset - (int) (condition.getHeight() * scaleFactor);
            default -> yOffset;
        };
    }

    public boolean isConditionMet() {
        if (condition == null) {
            condition = Condition.DEBUG_HUD_OPENED;
        }
        return this.condition.isConditionMet(additionalString);
    }

    public enum RenderMode {
        SHOW,
        HIDE,
        RENDER_IF_ACTIVE
    }

    public enum PositionMode {
        NONE,
        ADD_WIDTH,
        SUBTRACT_WIDTH,
        ADD_HEIGHT,
        SUBTRACT_HEIGHT
    }
}