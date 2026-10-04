package fin.starhud.init;

import com.mojang.blaze3d.platform.InputConstants;
import fin.starhud.Main;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class KeybindInit {

    public static void init() {
        Main.keyCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("starhud", "category"));

        Main.openEditHUDKey = new KeyMapping(
                "key.starhud.open_edithud",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_RSHIFT,
                Main.keyCategory
        );

        Main.toggleHUDKey = new KeyMapping(
                "key.starhud.toggle_hud",
                InputConstants.Type.KEYBOARD,
                InputConstants.UNKNOWN.getValue(),
                Main.keyCategory
        );
    }

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(Main.openEditHUDKey);
        event.register(Main.toggleHUDKey);
    }
}
