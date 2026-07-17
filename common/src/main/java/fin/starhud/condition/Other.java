package fin.starhud.condition;

import fin.starhud.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.level.Level;

public class Other {

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static boolean isModLoaded(String arg) {
        if (arg == null || arg.isEmpty()) return false;

        return Helper.isModLoaded(arg);
    }

    public static boolean isInSingleplayer(String ignored) {
        return CLIENT.isLocalServer();
    }

    public static boolean isOnServer(String arg) {
        if (arg == null || arg.isEmpty()) return false;

        ServerData entry = CLIENT.getCurrentServer();
        if (entry == null) return false;

        String serverIP = entry.ip.split(":")[0];

        return serverIP.equalsIgnoreCase(arg);
    }

    public static boolean isInOverworld(String ignored) {
        return CLIENT.player != null && CLIENT.player.level().dimension() == Level.OVERWORLD;
    }

    public static boolean isInNether(String ignored) {
        return CLIENT.player != null && CLIENT.player.level().dimension() == Level.NETHER;
    }

    public static boolean isInEnd(String ignored) {
        return CLIENT.player != null && CLIENT.player.level().dimension() == Level.END;
    }
}
