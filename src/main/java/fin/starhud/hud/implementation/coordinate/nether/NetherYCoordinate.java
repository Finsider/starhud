package fin.starhud.hud.implementation.coordinate.nether;

import fin.starhud.Main;
import fin.starhud.config.hud.CoordSettings;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.coordinate.AbstractCoordinateHUD;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class NetherYCoordinate extends AbstractCoordinateHUD {

    private static final CoordSettings SETTINGS = Main.settings.coordSettings.netherY;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/coordinate_y.png");

    public NetherYCoordinate() {
        super(SETTINGS, TEXTURE);
    }

    @Override
    public boolean shouldRender() {
        return super.shouldRender() && (CLIENT.player.getEntityWorld().getRegistryKey() == World.OVERWORLD || CLIENT.player.getEntityWorld().getRegistryKey() == World.NETHER);
    }

    @Override
    public int getCoord() {
        return (int) CLIENT.player.getPos().y;
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
