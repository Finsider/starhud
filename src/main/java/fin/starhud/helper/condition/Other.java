package fin.starhud.helper.condition;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.world.World;

public class Other {

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isModLoaded(String arg) {
        if (arg == null || arg.isEmpty()) return false;

        return FabricLoader.getInstance().isModLoaded(arg);
    }

    public static boolean isOnServer(String arg) {
        if (arg == null || arg.isEmpty()) return false;

        ServerInfo entry = CLIENT.getCurrentServerEntry();
        if (entry == null) return false;

        String serverIP = entry.address.split(":")[0];

        return serverIP.equalsIgnoreCase(arg);
    }

    public static boolean isInOverworld(String ignored) {
        return CLIENT.player.getWorld().getRegistryKey() == World.OVERWORLD;
    }

    public static boolean isInNether(String ignored) {
        return CLIENT.player.getWorld().getRegistryKey() == World.NETHER;
    }

    public static boolean isInEnd(String ignored) {
        return CLIENT.player.getWorld().getRegistryKey() == World.END;
    }
}
