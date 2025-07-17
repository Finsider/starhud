package fin.starhud.init;

import fin.starhud.Main;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindInit {

    public static void init() {
        Main.openEditHUDKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.starhud.open_edithud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.starhud"
        ));
    }
}
