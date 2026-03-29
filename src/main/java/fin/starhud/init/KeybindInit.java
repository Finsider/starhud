package fin.starhud.init;

import com.mojang.blaze3d.platform.InputConstants;
import fin.starhud.Main;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeybindInit {

    public static void init() {
        Main.keyCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("starhud", "category"));

        Main.openEditHUDKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.starhud.open_edithud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                Main.keyCategory
        ));

        Main.toggleHUDKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.starhud.toggle_hud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                Main.keyCategory
        ));
    }
}
