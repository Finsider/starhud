package fin.starhud.hud.implementation.coordinate.normal;

import fin.starhud.Main;
import fin.starhud.config.hud.coordinate.CoordSettings;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.coordinate.AbstractCoordinateHUD;
import net.minecraft.util.Identifier;

public class ZCoordinateHUD extends AbstractCoordinateHUD {
    private static final CoordSettings SETTINGS = Main.settings.coordSettings.Z;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/coordinate_z.png");

    public ZCoordinateHUD() {
        super(SETTINGS, TEXTURE);
    }

    @Override
    public int getCoord() {
        return (int) CLIENT.player.getEntityPos().z;
    }

    @Override
    public String getName() {
        return "Z Coordinate HUD";
    }

    @Override
    public String getId() {
        return HUDId.Z_COORDINATE.toString();
    }
}
