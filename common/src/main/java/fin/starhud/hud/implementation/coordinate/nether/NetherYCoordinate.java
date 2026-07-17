package fin.starhud.hud.implementation.coordinate.nether;

import fin.starhud.Main;
import fin.starhud.config.hud.coordinate.CoordSettings;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.coordinate.AbstractCoordinateHUD;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class NetherYCoordinate extends AbstractCoordinateHUD {

    private static final CoordSettings SETTINGS = Main.settings.coordSettings.netherY;
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/coordinate_y.png");

    public NetherYCoordinate() {
        super(SETTINGS, TEXTURE);
    }

    @Override
    public boolean shouldRender() {
        return super.shouldRender() && (CLIENT.player.level().dimension() == Level.OVERWORLD || CLIENT.player.level().dimension() == Level.NETHER);
    }

    @Override
    public int getCoord() {
        return (int) CLIENT.player.position().y;
    }

    @Override
    public String getName() {
        return "Nether Y Coordinate";
    }

    @Override
    public String getId() {
        return HUDId.NETHER_Y_COORDINATE.toString();
    }
}
