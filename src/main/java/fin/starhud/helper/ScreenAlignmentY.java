package fin.starhud.helper;

import net.minecraft.client.resource.language.I18n;

public enum ScreenAlignmentY {
    TOP,
    MIDDLE,
    BOTTOM;

    // if MIDDLE, place HUD in the middle of the screen
    public int getAlignmentPos(int scaledHeight) {
        return switch (this) {
            case TOP -> 0;
            case MIDDLE -> scaledHeight / 2;
            case BOTTOM -> scaledHeight;
        };
    }

    public ScreenAlignmentY next() {
        return switch (this) {
            case TOP -> MIDDLE;
            case MIDDLE -> BOTTOM;
            case BOTTOM -> TOP;
        };
    }

    public ScreenAlignmentY prev() {
        return switch (this) {
            case TOP -> BOTTOM;
            case MIDDLE -> TOP;
            case BOTTOM -> MIDDLE;
        };
    }

    @Override
    public String toString() {
        return I18n.translate("starhud.option.screenAlignmentY." + name().toLowerCase());
    }
}
