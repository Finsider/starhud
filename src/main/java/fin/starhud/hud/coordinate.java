package fin.starhud.hud;

import fin.starhud.Helper;
import fin.starhud.Main;
import fin.starhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class coordinate {

    private static final Settings.CoordSettings coord = Main.settings.coordSettings;
    private static final Identifier COORD_TEXTURE = Identifier.of("starhud", "hud/coordinate.png");

    private static final int[] X_OFFSETS = new int[3];
    private static final int[] Y_OFFSETS = new int[3];
    private static final boolean[] SHOULD_RENDER = new boolean[3];

    private static final int width = 65;
    private static final int height = 13;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void renderCoordinateHUD(DrawContext context) {
        if ((coord.hideOn.f3 && Helper.isDebugHUDOpen()) || (coord.hideOn.chat && Helper.isChatFocused())) return;

        initCoordinateConfiguration();
        TextRenderer textRenderer = client.textRenderer;

        Vec3d vec3d = client.player.getPos();

        String coordX = Integer.toString((int) vec3d.x);
        String coordY = Integer.toString((int) vec3d.y);
        String coordZ = Integer.toString((int) vec3d.z);

        int x = Helper.calculatePositionX(coord.x, coord.originX, width, coord.scale);
        int y = Helper.calculatePositionY(coord.y, coord.originY, height, coord.scale);

        int colorX = coord.coordXSettings.color | 0xFF000000;
        int colorY = coord.coordYSettings.color | 0xFF000000;
        int colorZ = coord.coordZSettings.color | 0xFF000000;

        context.getMatrices().push();
        Helper.setHUDScale(context, coord.scale);

        if (SHOULD_RENDER[0]) renderEachCoordinate(context, textRenderer, coordX, x + X_OFFSETS[0], y + Y_OFFSETS[0], 0.0F, width, height, colorX);
        if (SHOULD_RENDER[1]) renderEachCoordinate(context, textRenderer, coordY, x + X_OFFSETS[1], y + Y_OFFSETS[1], 14.0F, width, height, colorY);
        if (SHOULD_RENDER[2]) renderEachCoordinate(context, textRenderer, coordZ, x + X_OFFSETS[2], y + Y_OFFSETS[2], 28.0F, width, height, colorZ);

        context.getMatrices().pop();
    }

    public static void renderEachCoordinate(DrawContext context, TextRenderer textRenderer, String str, int x, int y, float v, int width, int height, int color) {
        context.drawTexture(RenderLayer::getGuiTextured, COORD_TEXTURE, x, y, 0.0F, v, width, height, width, 41, color);
        context.drawText(textRenderer, str, x + 19, y + 3, color, false);
    }

    private static void initCoordinateConfiguration() {
        X_OFFSETS[0] = coord.coordXSettings.xOffset; Y_OFFSETS[0] = coord.coordXSettings.yOffset;
        X_OFFSETS[1] = coord.coordYSettings.xOffset; Y_OFFSETS[1] = coord.coordYSettings.yOffset;
        X_OFFSETS[2] = coord.coordZSettings.xOffset; Y_OFFSETS[2] = coord.coordZSettings.yOffset;

        SHOULD_RENDER[0] = coord.coordXSettings.shouldRender;
        SHOULD_RENDER[1] = coord.coordYSettings.shouldRender;
        SHOULD_RENDER[2] = coord.coordZSettings.shouldRender;
    }
}