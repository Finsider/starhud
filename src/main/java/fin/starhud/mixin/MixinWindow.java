package fin.starhud.mixin;

import fin.starhud.hud.HUDComponent;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {

    // we update our HUD when the scaleWidth / scaleHeight is changed.
    // although quite silly, but This is the best performance we can get.
    @Inject(method = "setScaleFactor", at = @At("TAIL"))
    public void onScaleChanged(int scaleFactor, CallbackInfo ci) {
        HUDComponent.getInstance().updateAll();
    }

    @Inject(method = "onWindowSizeChanged", at = @At("TAIL"))
    public void onWindowSizeChanged(long window, int width, int height, CallbackInfo ci) {
        HUDComponent.getInstance().updateAll();
    }
}
