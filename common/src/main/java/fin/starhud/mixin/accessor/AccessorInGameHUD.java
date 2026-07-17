package fin.starhud.mixin.accessor;

import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Hud.class)
public interface AccessorInGameHUD {

    @Accessor("toolHighlightTimer")
    int getToolHighlightTimer();

}
