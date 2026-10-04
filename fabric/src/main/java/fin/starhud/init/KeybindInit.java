package fin.starhud.init;

import com.mojang.blaze3d.platform.InputConstants;
import fin.starhud.Main;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class KeybindInit {

    public static void init() {
        Main.keyCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("starhud", "category"));

        Main.openEditHUDKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.starhud.open_edithud",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_RSHIFT,
                Main.keyCategory
        ));

        Main.toggleHUDKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.starhud.toggle_hud",
                InputConstants.Type.KEYBOARD,
                InputConstants.UNKNOWN.getValue(),
                Main.keyCategory
        ));
    }
}
