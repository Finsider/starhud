package fin.starhud.hud.implementation.coordinate.nether;

import fin.starhud.Main;
import fin.starhud.config.hud.coordinate.CoordSettings;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.coordinate.AbstractCoordinateHUD;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NetherZCoordinate extends AbstractCoordinateHUD {

    private static final CoordSettings SETTINGS = Main.settings.coordSettings.netherZ;
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/coordinate_z.png");

    public NetherZCoordinate() {
        super(SETTINGS, TEXTURE);
    }

    @Override
    public boolean shouldRender() {
        return super.shouldRender() && (CLIENT.player.level().dimension() == Level.OVERWORLD || CLIENT.player.level().dimension() == Level.NETHER);
    }

    @Override
    public int getCoord() {
        Level world = CLIENT.player.level();
        Vec3 pos = CLIENT.player.position();

        if (world.dimension() == Level.NETHER) {
            return (int) (pos.z * 8);
        } else if (world.dimension() == Level.OVERWORLD) {
            return (int) (pos.z / 8);
        } else {
            return (int) pos.z;
        }
    }

    @Override
    public String getName() {
        return "Nether Z Coordinate";
    }

    @Override
    public String getId() {
        return HUDId.NETHER_Z_COORDINATE.toString();
    }
}
