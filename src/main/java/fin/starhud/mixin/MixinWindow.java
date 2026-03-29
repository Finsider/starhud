package fin.starhud.mixin;

import com.mojang.blaze3d.platform.Window;
import fin.starhud.hud.HUDComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {

    @Inject(method = "onFramebufferResize", at = @At("TAIL"))
    public void onFramebufferSizeChanged(long window, int width, int height, CallbackInfo ci) {
        HUDComponent.getInstance().updateAll();
    }
}
