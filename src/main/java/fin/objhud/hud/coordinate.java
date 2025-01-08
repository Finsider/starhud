package fin.objhud.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import fin.objhud.Helper;
import fin.objhud.Main;
import fin.objhud.config.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class coordinate {

    public static Settings.CoordSettings coord = Main.settings.coordSettings;
    private static final Identifier HUD_TEXTURE = Identifier.of("objhud", "hud/coordinate.png");

    public static void renderCoordinateHUD(DrawContext context) {
        if (!coord.renderCoordinateHUD) return;

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        Vec3d vec3d = client.player.getPos();

        String coordX = Integer.toString((int) vec3d.x);
        String coordY = Integer.toString((int) vec3d.y);
        String coordZ = Integer.toString((int) vec3d.z);

        int x = Helper.defaultHUDLocationX(coord.defX, context) + coord.x;
        int y = Helper.defaultHUDLocationY(coord.defY, context) + coord.y;

        int colorX = coord.color.X | 0xFF000000;
        int colorY = coord.color.Y | 0xFF000000;
        int colorZ = coord.color.Z | 0xFF000000;

        renderEachCoordinate(context, textRenderer, coordX, x, y, 0.0F, colorX);
        renderEachCoordinate(context, textRenderer, coordY, x, y + 14, 14.0F, colorY);
        renderEachCoordinate(context, textRenderer, coordZ, x, y + 28, 28.0F, colorZ);
    }
    public static void renderEachCoordinate(DrawContext context, TextRenderer textRenderer, String str, int x, int y, float v, int color) {
        Helper.drawTextureAlphaColor(context, HUD_TEXTURE, x, y, 0.0F, v, 65, 14, 65, 41, color);
        context.drawText(textRenderer, str, x + 19, y + 3, color, false);
    }
}