package fin.starhud.mixin.accessor;

import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerListHud.class)
public interface AccessorPlayerListHud {

    @Accessor("visible")
    boolean isVisible();
}
