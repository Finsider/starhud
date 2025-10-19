package fin.starhud.init;

import fin.starhud.Main;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeybindInit {

    public static void init() {
        Main.keyCategory = KeyBinding.Category.create(Identifier.of("starhud", "category"));

        Main.openEditHUDKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.starhud.open_edithud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                Main.keyCategory
        ));

        Main.toggleHUDKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.starhud.toggle_hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                Main.keyCategory
        ));
    }
}
