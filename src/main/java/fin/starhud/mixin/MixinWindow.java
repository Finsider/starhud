package fin.starhud.mixin;

import fin.starhud.hud.HUDComponent;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {

    @Inject(method = "onFramebufferSizeChanged", at = @At("TAIL"))
    public void onFramebufferSizeChanged(long window, int width, int height, CallbackInfo ci) {
        HUDComponent.getInstance().updateAll();
    }
}
