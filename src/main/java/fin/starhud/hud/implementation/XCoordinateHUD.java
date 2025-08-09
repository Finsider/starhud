package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.CoordSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.util.Identifier;

public class XCoordinateHUD extends AbstractCoordinateHUD {

    private static final CoordSettings SETTINGS = Main.settings.coordSettings.X;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/coordinate_x.png");

    public XCoordinateHUD() {
        super(SETTINGS, TEXTURE);
    }

    @Override
    public int getCoord() {
        return (int) CLIENT.player.getPos().x;
    }

    @Override
    public String getName() {
        return "X Coordinate HUD";
    }

    @Override
    public String getId() {
        return HUDId.X_COORDINATE.toString();
    }
}
