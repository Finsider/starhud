package fin.starhud.mixin.accessor;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface AccessorInGameHUD {

    @Accessor("toolHighlightTimer")
    int getToolHighlightTimer();

}
