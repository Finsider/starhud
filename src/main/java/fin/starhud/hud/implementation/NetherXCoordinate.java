package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.CoordSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class NetherXCoordinate extends AbstractCoordinateHUD {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final CoordSettings SETTINGS = Main.settings.coordSettings.netherX;
    private static final Identifier TEXTURE = Identifier.of("starhud", "hud/coordinate_x.png");

    public NetherXCoordinate() {
        super(SETTINGS, TEXTURE);
    }

    @Override
    public boolean shouldRender() {
        return super.shouldRender() && (CLIENT.player.getWorld().getRegistryKey() == World.OVERWORLD || CLIENT.player.getWorld().getRegistryKey() == World.NETHER);
    }

    @Override
    public int getCoord() {
        World world = CLIENT.player.getWorld();
        Vec3d pos = CLIENT.player.getPos();

        if (world.getRegistryKey() == World.NETHER) {
            return (int) (pos.x * 8);
        } else if (world.getRegistryKey() == World.OVERWORLD) {
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
