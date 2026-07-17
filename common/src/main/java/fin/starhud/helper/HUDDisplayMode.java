package fin.starhud.helper;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import net.minecraft.client.resources.language.I18n;

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

    @Override
    public String toString() {
        return I18n.get("starhud.option.hudDisplayMode." + name().toLowerCase());
    }
}
