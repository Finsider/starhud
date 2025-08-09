package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.CoordSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.util.Identifier;

public class YCoordinateHUD extends AbstractCoordinateHUD {

    private static final CoordSettings SETTINGS = Main.settings.coordSettings.Y;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/coordinate_y.png");

    public YCoordinateHUD() {
        super(SETTINGS, TEXTURE);
    }

    @Override
    public int getCoord() {
        return (int) CLIENT.player.getPos().y;
    }

    @Override
    public String getName() {
        return "Y Coordinate HUD";
    }

    @Override
    public String getId() {
        return HUDId.Y_COORDINATE.toString();
    }
}
