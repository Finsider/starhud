package fin.starhud.screen;

import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class HelpWidget {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final boolean isMac = EditHUDScreen.isMac;

    private record Help(Text key, Text info) {}

    private static final Help[] HELPS = {
            new Help(Text.translatable("starhud.help.key.move_1"), Text.translatable("starhud.help.info.move_1")),
            new Help(Text.translatable("starhud.help.key.move_5"), Text.translatable("starhud.help.info.move_5")),
            new Help(Text.translatable(isMac ? "starhud.help.key.alignment_mac" : "starhud.help.key.alignment_windows"), Text.translatable("starhud.help.info.alignment")),
            new Help(Text.translatable("starhud.help.key.direction"), Text.translatable("starhud.help.info.direction")),
            new Help(Text.translatable(isMac ? "starhud.help.key.revert_changes_mac" : "starhud.help.key.revert_changes_windows"), Text.translatable("starhud.help.info.revert_changes")),
            new Help(Text.translatable("starhud.help.key.group_ungroup"), Text.translatable("starhud.help.info.group_ungroup")),
            new Help(Text.translatable("starhud.help.key.clamp_all"), Text.translatable("starhud.help.info.clamp_all")),
            new Help(Text.translatable("starhud.help.key.undo"), Text.translatable("starhud.help.info.undo")),
            new Help(Text.translatable("starhud.help.key.redo"), Text.translatable("starhud.help.info.redo"))
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
            maxKey = Math.max(maxKey, CLIENT.textRenderer.getWidth(h.key));
            maxInfo = Math.max(maxInfo, CLIENT.textRenderer.getWidth(h.info));
        }

        HELP_KEY_MAX_WIDTH = maxKey;
        HELP_INFO_MAX_WIDTH = maxInfo;
    }

    public void render(DrawContext context, AbstractHUD hud, int x, int y) {
        int padding = 5;

        int lineHeight = CLIENT.textRenderer.fontHeight;

        int maxKeyWidth = HELP_KEY_MAX_WIDTH;
        int maxInfoWidth = HELP_INFO_MAX_WIDTH;

        int width = padding + maxKeyWidth + padding + 1 + padding + maxInfoWidth + padding;
        int height = padding + (lineHeight * HELPS.length) + padding - 2;

        int helpX = x - width / 2;
        int helpY = y - padding;

        context.fill(helpX, helpY, helpX + width, helpY + height, 0x80000000);

        for (Help h : HELPS) {
            Text key = h.key;
            Text info = h.info;

            context.drawText(CLIENT.textRenderer, key, helpX + padding, helpY + padding, 0xFFFFFFFF, false);
            context.drawText(CLIENT.textRenderer, info, helpX + padding + maxKeyWidth + padding + 1 + padding, helpY + padding, 0xFFFFFFFF, false);

            helpY += lineHeight;
        }

        if (hud != null)
            renderHUDInformation(context, hud, x, y + HELP_HEIGHT + GAP);
    }

    private void renderHUDInformation(DrawContext context, AbstractHUD hud, int x, int y) {
        String text = hud.getName();
        int textWidth = CLIENT.textRenderer.getWidth(text);
        int padding = 5;

        int infoX = x - (textWidth / 2);

        context.fill(infoX - padding, y - padding, infoX + textWidth + padding, y + CLIENT.textRenderer.fontHeight - 2 + padding, 0x80000000);
        context.drawText(CLIENT.textRenderer, text, infoX, y, 0xFFFFFFFF, false);
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return this.isActive;
    }
}
