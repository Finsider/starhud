package fin.starhud.hud.implementation.coordinate.nether;

import fin.starhud.Main;
import fin.starhud.config.hud.coordinate.CoordSettings;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.coordinate.AbstractCoordinateHUD;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class NetherXCoordinate extends AbstractCoordinateHUD {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final CoordSettings SETTINGS = Main.settings.coordSettings.netherX;
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("starhud", "hud/coordinate_x.png");

    public NetherXCoordinate() {
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
            return (int) (pos.x * 8);
        } else if (world.dimension() == Level.OVERWORLD) {
            return (int) (pos.x / 8);
        } else {
            return (int) pos.x;
        }
    }

    @Override
    public String getName() {
        return "Nether X Coordinate";
    }

    @Override
    public String getId() {
        return HUDId.NETHER_X_COORDINATE.toString();
    }
}
