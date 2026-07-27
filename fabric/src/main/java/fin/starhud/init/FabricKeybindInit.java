package fin.starhud.init;

import com.mojang.blaze3d.platform.InputConstants;
import fin.starhud.Main;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class FabricKeybindInit {

    public static void init() {
        Main.openEditHUDKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.starhud.open_edithud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "key.category.starhud.category"
        ));

        Main.toggleHUDKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.starhud.toggle_hud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.category.starhud.category"
        ));
    }
}
