package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.CoordSettings;
import fin.starhud.helper.Box;
import fin.starhud.helper.RenderUtils;
import fin.starhud.hud.AbstractHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class CoordinateHUD extends AbstractHUD {

    private static final CoordSettings COORD_SETTINGS = Main.settings.coordSettings;

    private static final Identifier COORD_TEXTURE = Identifier.of("starhud", "hud/coordinate.png");

    private static final int TEXTURE_WIDTH = 65;
    private static final int TEXTURE_HEIGHT = 13;

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static boolean needBoxUpdate = true;
    private static final Box tempBox = new Box(0, 0);

    public CoordinateHUD() {
        super(COORD_SETTINGS.base);
    }

    @Override
    public boolean shouldRender() {
        return super.shouldRender()
                && (COORD_SETTINGS.X.shouldRender && COORD_SETTINGS.Y.shouldRender && COORD_SETTINGS.Z.shouldRender);
    }

    @Override
    public String getName() {
        return "Coordinate HUD";
    }

    @Override
    public boolean renderHUD(DrawContext context) {
        Vec3d vec3d = CLIENT.player.getPos();

        String coordX = Integer.toString((int) vec3d.x);
        String coordY = Integer.toString((int) vec3d.y);
        String coordZ = Integer.toString((int) vec3d.z);

        int colorX = COORD_SETTINGS.X.color | 0xFF000000;
        int colorY = COORD_SETTINGS.Y.color | 0xFF000000;
        int colorZ = COORD_SETTINGS.Z.color | 0xFF000000;

        if (COORD_SETTINGS.X.shouldRender)
            renderEachCoordinate(context, coordX, x + COORD_SETTINGS.X.xOffset, y + COORD_SETTINGS.X.yOffset, 0.0F, TEXTURE_WIDTH, TEXTURE_HEIGHT, colorX);

        if (COORD_SETTINGS.Y.shouldRender)
            renderEachCoordinate(context, coordY, x + COORD_SETTINGS.Y.xOffset, y + COORD_SETTINGS.Y.yOffset, 14.0F, TEXTURE_WIDTH, TEXTURE_HEIGHT, colorY);

        if (COORD_SETTINGS.Z.shouldRender)
            renderEachCoordinate(context, coordZ, x + COORD_SETTINGS.Z.xOffset, y + COORD_SETTINGS.Z.yOffset, 28.0F, TEXTURE_WIDTH, TEXTURE_HEIGHT, colorZ);

        needBoxUpdate = false;
        return true;
    }

    @Override
    public void update() {
        super.update();

        needBoxUpdate = true;
        super.boundingBox.setEmpty(true);
    }

    @Override
    public int getBaseHUDWidth() {
        return TEXTURE_WIDTH;
    }

    @Override
    public int getBaseHUDHeight() {
        return TEXTURE_HEIGHT;
    }

    public void renderEachCoordinate(DrawContext context, String str, int x, int y, float v, int width, int height, int color) {
        RenderUtils.drawTextureHUD(context, COORD_TEXTURE, x, y, 0.0F, v, width, height, width, 41, color);
        RenderUtils.drawTextHUD(context, str, x + 19, y + 3, color, false);

        tempBox.setBoundingBox(x, y, width, height, color);
        if (needBoxUpdate) {
            if (super.boundingBox.isEmpty())
                super.boundingBox.setBoundingBox(tempBox.getX(), tempBox.getY(), tempBox.getWidth(), tempBox.getHeight(), tempBox.getColor());
            else
                super.boundingBox.mergeWith(tempBox);
        }
    }
}
