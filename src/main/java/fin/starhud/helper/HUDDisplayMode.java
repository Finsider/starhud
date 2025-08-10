package fin.starhud.helper;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;

public enum HUDDisplayMode {
    ICON,
    INFO,
    BOTH;

    public int calculateWidth(int iconWidth, int infoWidth) {
        final GeneralSettings.HUDSettings SETTINGS = Main.settings.generalSettings.hudSettings;
        int padding = SETTINGS.textPadding;
        int gap = SETTINGS.iconInfoGap;

        return switch (this) {
            case ICON -> iconWidth;
            case INFO -> padding + infoWidth + padding;
            case BOTH -> iconWidth + gap + padding + infoWidth + padding;
        };
    }

    public HUDDisplayMode next() {
        return switch (this) {
            case ICON -> INFO;
            case INFO -> BOTH;
            case BOTH -> ICON;
        };
    }
}
