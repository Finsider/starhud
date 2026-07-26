package fin.starhud.init;

import com.mojang.blaze3d.platform.InputConstants;
import fin.starhud.Main;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeybindInit {

    public static void init() {
        Main.openEditHUDKey = new KeyMapping(
                "key.starhud.open_edithud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "key.category.starhud.category"
        );

        Main.toggleHUDKey = new KeyMapping(
                "key.starhud.toggle_hud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.category.starhud.category"
        );
    }

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(Main.openEditHUDKey);
        event.register(Main.toggleHUDKey);
    }
}
