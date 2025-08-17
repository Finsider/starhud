package fin.starhud.helper;

import net.minecraft.client.resource.language.I18n;

public enum GrowthDirectionY {
    UP,
    MIDDLE,
    DOWN;

    public int getGrowthDirection(int dynamicHeight) {
        return switch (this) {
            case UP -> dynamicHeight;
            case MIDDLE -> dynamicHeight / 2;
            case DOWN -> 0;
        };
    }

    public GrowthDirectionY next() {
        return switch (this) {
            case UP -> MIDDLE;
            case MIDDLE -> DOWN;
            case DOWN -> UP;
        };
    }

    public GrowthDirectionY prev() {
        return switch (this) {
            case UP -> DOWN;
            case MIDDLE -> UP;
            case DOWN -> MIDDLE;
        };
    }

    public GrowthDirectionY recommendedScreenAlignment(ScreenAlignmentY screenAlignmentY) {
        return switch (screenAlignmentY) {
            case TOP -> DOWN;
            case MIDDLE -> MIDDLE;
            case BOTTOM -> UP;
        };
    }

    @Override
    public String toString() {
        return I18n.translate("starhud.option.growthDirectionY." + name().toLowerCase());
    }
}
