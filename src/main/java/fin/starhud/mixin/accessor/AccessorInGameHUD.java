package fin.starhud.mixin.accessor;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface AccessorInGameHUD {

    @Accessor("heldItemTooltipFade")
    int getHeldItemTooltipFade();

}
