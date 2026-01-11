package fin.starhud.hud.implementation.other;

import fin.starhud.Main;
import fin.starhud.config.hud.other.InventorySpaceSettings;
import fin.starhud.helper.HUDDisplayMode;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class InventorySpaceHUD extends AbstractHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/inventory_space.png");
    private static final InventorySpaceSettings SETTINGS = Main.settings.inventorySpaceSettings;

    private static final int TEXTURE_WIDTH = 13;
    private static final int TEXTURE_HEIGHT = 13;
    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private String str;
    private HUDDisplayMode displayMode;

    public InventorySpaceHUD() {
        super(SETTINGS.base);
    }

    @Override
    public boolean collectHUDInformation() {
        if (CLIENT.player == null) return false;

        displayMode = getSettings().getDisplayMode();

        int filledSlot = 0;
        for (ItemStack stack : CLIENT.player.getInventory().main)
            if (!stack.isEmpty())
                ++filledSlot;

        int maxSlot = CLIENT.player.getInventory().main.size();
        int emptySlot = maxSlot - filledSlot;

        int slot = SETTINGS.showRemaining ? emptySlot : filledSlot;
        if (SETTINGS.showMaxSlot)
            str = slot + "/" + maxSlot;
        else
            str = Integer.toString(slot);

        int strWidth = CLIENT.textRenderer.getWidth(str) - 1;

        int width = displayMode.calculateWidth(ICON_WIDTH, strWidth);
        int color = SETTINGS.color | 0xFF000000;

        setWidthHeightColor(width, TEXTURE_HEIGHT, color);

        return str != null;
    }

    @Override
    public boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground, boolean drawTextShadow) {
        int w = getWidth(), h = getHeight(), c = getColor();

        return RenderUtils.drawSmallHUD(
                context,
                str,
                x, y,
                w, h,
                TEXTURE,
                0, 0,
                TEXTURE_WIDTH, TEXTURE_HEIGHT,
                ICON_WIDTH, ICON_HEIGHT,
                c,
                displayMode,
                drawBackground,
                drawTextShadow
        );
    }

    @Override
    public String getName() {
        return "Inventory Space HUD";
    }

    @Override
    public String getId() {
        return HUDId.INVENTORY_SPACE.toString();
    }
}
