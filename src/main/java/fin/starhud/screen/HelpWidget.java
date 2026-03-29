package fin.starhud.screen;

import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class HelpWidget {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final boolean isMac = EditHUDScreen.isMac;

    private record Help(Component key, Component info) {}

    private static final Help[] HELPS = {
            new Help(Component.translatable("starhud.help.key.move_1"), Component.translatable("starhud.help.info.move_1")),
            new Help(Component.translatable("starhud.help.key.move_5"), Component.translatable("starhud.help.info.move_5")),
            new Help(Component.translatable(isMac ? "starhud.help.key.alignment_mac" : "starhud.help.key.alignment_windows"), Component.translatable("starhud.help.info.alignment")),
            new Help(Component.translatable("starhud.help.key.direction"), Component.translatable("starhud.help.info.direction")),
            new Help(Component.translatable("starhud.help.key.group_ungroup"), Component.translatable("starhud.help.info.group_ungroup")),
            new Help(Component.translatable("starhud.help.key.clamp_all"), Component.translatable("starhud.help.info.clamp_all")),
            new Help(Component.translatable(isMac ? "starhud.help.key.undo_mac" : "starhud.help.key.undo_windows"), Component.translatable("starhud.help.info.undo")),
            new Help(Component.translatable(isMac ? "starhud.help.key.redo_mac" : "starhud.help.key.redo_windows"), Component.translatable("starhud.help.info.redo")),
            new Help(Component.translatable(isMac ? "starhud.help.key.save_mac" : "starhud.help.key.save_windows"), Component.translatable("starhud.help.info.save")),
            new Help(Component.translatable(isMac ? "starhud.help.key.reset_mac" : "starhud.help.key.reset_windows"), Component.translatable("starhud.help.info.reset"))
    };

    private static int HELP_KEY_MAX_WIDTH;
    private static int HELP_INFO_MAX_WIDTH;
    private static final int HELP_HEIGHT = 5 + (HELPS.length * 9) + 5;
    private static final int GAP = EditHUDScreen.GAP;

    private boolean isActive = false;

    public HelpWidget() {
        getHelpMaxWidths();
    }

    private void getHelpMaxWidths() {
        int maxKey = 0;
        int maxInfo = 0;

        for (Help h : HELPS) {
            maxKey = Math.max(maxKey, CLIENT.font.width(h.key));
            maxInfo = Math.max(maxInfo, CLIENT.font.width(h.info));
        }

        HELP_KEY_MAX_WIDTH = maxKey;
        HELP_INFO_MAX_WIDTH = maxInfo;
    }

    public void render(GuiGraphicsExtractor context, AbstractHUD hud, int x, int y) {
        int padding = 5;

        int lineHeight = CLIENT.font.lineHeight;

        int maxKeyWidth = HELP_KEY_MAX_WIDTH;
        int maxInfoWidth = HELP_INFO_MAX_WIDTH;

        int width = padding + maxKeyWidth + padding + 1 + padding + maxInfoWidth + padding;
        int height = padding + (lineHeight * HELPS.length) + padding - 2;

        int helpX = x - width / 2;
        int helpY = y - padding;

        context.fill(helpX, helpY, helpX + width, helpY + height, 0x80000000);

        for (Help h : HELPS) {
            Component key = h.key;
            Component info = h.info;

            context.text(CLIENT.font, key, helpX + padding, helpY + padding, 0xFFFFFFFF, false);
            context.text(CLIENT.font, info, helpX + padding + maxKeyWidth + padding + 1 + padding, helpY + padding, 0xFFFFFFFF, false);

            helpY += lineHeight;
        }

        if (hud != null)
            renderHUDInformation(context, hud, x, y + HELP_HEIGHT + GAP);
    }

    private void renderHUDInformation(GuiGraphicsExtractor context, AbstractHUD hud, int x, int y) {
        String text = hud.getName();
        int textWidth = CLIENT.font.width(text);
        int padding = 5;

        int infoX = x - (textWidth / 2);

        context.fill(infoX - padding, y - padding, infoX + textWidth + padding, y + CLIENT.font.lineHeight - 2 + padding, 0x80000000);
        context.text(CLIENT.font, text, infoX, y, 0xFFFFFFFF, false);
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return this.isActive;
    }
}
